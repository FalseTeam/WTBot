package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import team.false_.wtbot.helpers.accessLevel
import team.false_.wtbot.helpers.isCommandChannel
import javax.inject.Inject

class AccessLevelHandler @Inject constructor() : Handler() {
    override fun subscribe(): Disposable {
        return manager.on<MessageReceivedEvent>()
            .map { it.message }
            .filter { it.channel.isCommandChannel && it.contentRaw.startsWith("!al") }
            .map { it.channel.sendMessage("Access Level: ${it.member!!.accessLevel}") }
            .flatMap { it.asMono() }
            .subscribe()
    }
}