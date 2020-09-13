package team.false_.wtbot.handlers.roles

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.util.function.Tuples
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.handlers.Handler
import team.false_.wtbot.helpers.*

class AddRoleHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReceivedEvent> { event ->
            Flux.just(event).map { it.message }
                .filterCommand("addrole")
                .requireAccessLevel(AccessLevel.JUNIOR)
                .requireMembersAndRolesMentions()
                .map {
                    Tuples.of(
                        it.author,
                        it.channel,
                        it.mentionedMembers,
                        it.mentionedRolesAllowed,
                        it.mentionedRolesDenied
                    )
                }
                .addRoles()
        }
    }
}