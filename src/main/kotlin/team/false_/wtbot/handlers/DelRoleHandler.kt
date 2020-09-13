package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.Main
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.helpers.*

class DelRoleHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReceivedEvent> { event ->
            Flux.just(event).map { it.message }
                .filterCommand("delrole")
                .requireAccessLevel(AccessLevel.JUNIOR)
                .requireMembersAndRolesMentions()
                .flatMap {
                    val rAllowed = it.mentionedRolesAllowed

                    val sUsers = it.mentionedUsers.joinDefault()
                    val sAllowed = rAllowed.joinDefault()
                    val sDenied = it.mentionedRolesDenied.joinDefault()

                    val text = ArrayList<String>(3)
                        .apply { add("Users: $sUsers") }
                        .apply { if (sAllowed.isNotEmpty()) add("Removed Roles: $sAllowed") }
                        .apply { if (sDenied.isNotEmpty()) add("Not Removed Roles: $sDenied") }
                        .joinToString("\n")

                    Main.log.info("[DelRole] ${it.author.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

                    Flux.concat(
                        it.mentionedMembers.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::delRole) },
                        it.channel.sendSuccess(text).asMono(),
                        it.jda.logStaff(it.author, "Del Role", text).asMono()
                    )
                }
        }
    }
}