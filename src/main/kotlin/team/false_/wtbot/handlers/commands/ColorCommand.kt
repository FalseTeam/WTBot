package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import java.util.regex.Pattern

class ColorCommand : AdminCommand() {
    private val pattern = Pattern.compile("[0-9A-Fa-f]{6}")

    override fun exec(it: Message): Flux<out Any> {
        val matcher = pattern.matcher(it.contentRaw)
        val list = ArrayList<Mono<Message>>()
        while (matcher.find()) {
            val sColor = matcher.group()
            list.add(
                it.channel.sendMessage(
                    EmbedBuilder()
                        .setColor(sColor.toInt(16))
                        .setDescription(sColor)
                        .build()
                ).asMono()
            )
        }
        return Flux.concat(
            list.toFlux().flatMap { it },
            it.delete().asMono()
        )
    }
}