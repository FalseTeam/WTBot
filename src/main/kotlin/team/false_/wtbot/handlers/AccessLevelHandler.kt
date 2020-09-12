package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import team.false_.wtbot.helpers.accessLevel
import team.false_.wtbot.helpers.isCommandChannel
import team.false_.wtbot.helpers.subscribeDefault
import java.awt.Color
import javax.inject.Inject

class AccessLevelHandler @Inject constructor() : Handler() {
    override fun subscribe(): Disposable {
        return manager.on<MessageReceivedEvent>()
            .map { it.message }
            .filter { it.channel.isCommandChannel && it.contentRaw.startsWith("!al") }
            .flatMap {
                it.channel.sendMessage(
                    EmbedBuilder()
                        .setColor(Color(121, 68, 59))
                        .setAuthor(it.author.asTag, null, it.author.avatarUrl)
                        .setDescription("Access Level is ${it.member!!.accessLevel}")
                        .build()
                ).asMono()
            }
            .subscribeDefault()
    }
}