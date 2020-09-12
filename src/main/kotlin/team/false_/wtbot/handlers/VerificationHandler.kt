package team.false_.wtbot.handlers

import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import reactor.core.Disposable
import team.false_.wtbot.Config.MESSAGE_ACCEPT_ID
import team.false_.wtbot.Config.ROLE_GREETING_ID
import team.false_.wtbot.helpers.addRole
import team.false_.wtbot.helpers.subscribeDefault
import javax.inject.Inject

class VerificationHandler @Inject constructor() : Handler() {
    override fun subscribe(): Disposable {
        return manager.on<MessageReactionAddEvent>()
            .filter { it.messageIdLong == MESSAGE_ACCEPT_ID }
            .flatMap { it.member!!.addRole(it.guild.getRoleById(ROLE_GREETING_ID)!!) }
            .subscribeDefault()
    }
}