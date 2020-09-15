package team.false_.wtbot.handlers

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Messages
import team.false_.wtbot.config.Roles
import team.false_.wtbot.utils.exec
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class VerificationHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReactionAddEvent> { event ->
            Flux.just(event)
                .filter { it.messageIdLong == Messages.ACCEPT_RULES }
                .flatMap { it.member!!.exec(it.guild.getRoleById(Roles.LVL1)!!) }
        }
    }
}