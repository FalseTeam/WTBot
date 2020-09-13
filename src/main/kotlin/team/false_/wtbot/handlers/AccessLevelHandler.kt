package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.helpers.accessLevel
import team.false_.wtbot.helpers.filterCommand
import team.false_.wtbot.helpers.subscribeOnAnyWithHandleError
import java.awt.Color

class AccessLevelHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReceivedEvent> { event ->
            Flux.just(event).map { it.message }
                .filterCommand("al")
                .flatMap {
                    it.channel.sendMessage(
                        EmbedBuilder()
                            .setColor(Color(121, 68, 59))
                            .setAuthor(it.author.asTag, null, it.author.avatarUrl)
                            .setDescription("Access Level is ${it.member!!.accessLevel}")
                            .build()
                    ).asMono()
                }
        }
    }
}