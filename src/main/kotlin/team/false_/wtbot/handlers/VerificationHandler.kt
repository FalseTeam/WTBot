package team.false_.wtbot.handlers

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.Config.MESSAGE_ACCEPT_ID
import team.false_.wtbot.Config.ROLE_GREETING_ID
import team.false_.wtbot.helpers.addRole
import team.false_.wtbot.helpers.subscribeOnAnyWithHandleError

class VerificationHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReactionAddEvent> { event ->
            Flux.just(event)
                .filter { it.messageIdLong == MESSAGE_ACCEPT_ID }
                .flatMap { it.member!!.addRole(it.guild.getRoleById(ROLE_GREETING_ID)!!) }
        }
    }
}