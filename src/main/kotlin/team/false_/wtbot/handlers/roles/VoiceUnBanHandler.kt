package team.false_.wtbot.handlers.roles

import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.util.function.Tuples
import team.false_.wtbot.config.Roles
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.handlers.Handler
import team.false_.wtbot.helpers.*

class VoiceUnBanHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReceivedEvent> { event ->
            Flux.just(event).map { it.message }
                .filterCommand("unvb")
                .requireAccessLevel(AccessLevel.JUNIOR)
                .requireMembersMentions()
                .map {
                    Tuples.of(
                        it.author,
                        it.channel,
                        it.mentionedMembers,
                        listOf(it.jda.getRoleById(Roles.VOICE_BANNED)!!),
                        listOf<Role>()
                    )
                }
                .removeRoles()
        }
    }
}