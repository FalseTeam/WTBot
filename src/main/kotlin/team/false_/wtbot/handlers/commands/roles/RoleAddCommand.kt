package team.false_.wtbot.handlers.commands.roles

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.*
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.Main
import team.false_.wtbot.config.Colors
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.handlers.commands.abstract.StaffCommand
import team.false_.wtbot.utils.*

class RoleAddCommand : StaffCommand() {
    override fun exec(it: Message): Flux<out Any> {
        return if (it.mentionedMembers.isEmpty() or it.mentionedRoles.isEmpty())
            Flux.error(EmptyMentionsException.default)
        else
            exec(
                it.author,
                it.channel,
                it.mentionedMembers,
                it.mentionedRolesAllowed,
                it.mentionedRolesDenied
            )
    }

    companion object {
        fun exec(
            subject: User,
            channel: MessageChannel,
            members: List<Member>,
            rAllowed: List<Role>,
            rDenied: List<Role>
        ): Flux<out Any> {
            val sUsers = members.joinDefault()
            val sAllowed = rAllowed.joinDefault()
            val sDenied = rDenied.joinDefault()

            val text = ArrayList<String>(3)
                .apply { add("Users: $sUsers") }
                .apply { if (sAllowed.isNotEmpty()) add("Granted Roles: $sAllowed") }
                .apply { if (sDenied.isNotEmpty()) add("Not Granted Roles: $sDenied") }
                .joinToString("\n")

            Main.log.info("[AddRole] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

            return Flux.concat(
                members.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::exec) },
                channel.sendSuccess(text).asMono(),
                channel.jda.logStaff(subject, "Add Role", text, Colors.ROLE_ADD).asMono()
            )
        }
    }
}