package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import java.util.regex.Pattern
import kotlin.random.Random

class ColorRandomCommand : AdminCommand() {
    val pattern = Pattern.compile("[0-9]+")

    override fun exec(it: Message): Flux<out Any> {
        val matcher = pattern.matcher(it.contentRaw)
        val count = if (matcher.find()) matcher.group().toLong() else 5
        return Flux.generate<Mono<Message>> { sink ->
            val color = Random.nextInt(0xFFFFFF)
            sink.next(
                it.channel.sendMessage(
                    EmbedBuilder()
                        .setColor(color)
                        .setDescription(color.toString(16).toUpperCase())
                        .build()
                ).asMono()
            )
        }.take(count).flatMap { it }
    }
}