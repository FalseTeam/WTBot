package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asFlux
import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.config.Channels
import team.false_.wtbot.config.Colors
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.exceptions.IllegalArgumentException
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import team.false_.wtbot.utils.joinNewLine
import java.time.Instant

class PurgeCommand : AdminCommand() {
    private val allowed = setOf(
        Channels.DEV_INPUT,
        Channels.DEV_OUTPUT,
        Channels.DEV_ERROR
    )

    private val fAllowed: (TextChannel) -> Boolean = { it.idLong in allowed }

    private fun purgeChannel(channel: TextChannel): Flux<Void> {
        return channel.iterableHistory.cache(false).asMono()
            .flatMapMany { channel.purgeMessages(it).asFlux() }
    }

    private fun embed(subject: User, description: String): MessageEmbed {
        return EmbedBuilder()
            .setColor(Colors.SUCCESS)
            .setTitle("Purge")
            .setDescription(description)
            .setFooter(subject.asTag, subject.avatarUrl)
            .setTimestamp(Instant.now())
            .build()
    }

    override fun exec(it: Message): Flux<out Any> {
        if (it.mentionedChannels.isEmpty()) return Flux.error(EmptyMentionsException.channels)

        val lAllowed = it.mentionedChannels.filter(fAllowed)
        if (lAllowed.isEmpty()) return Flux.error(IllegalArgumentException.noAllowedChannelsFound)

        val sAllowed = lAllowed.joinNewLine()
        val sIgnored = it.mentionedChannels.filterNot(fAllowed).joinNewLine()

        val text = if (sIgnored.isEmpty()) sAllowed else "$sAllowed\n\nIgnored\n$sIgnored"

        return it.channel.sendMessage(embed(it.author, "Started")).asMono()
            .flatMapMany { message ->
                Flux.concat(
                    lAllowed.toFlux().flatMap(::purgeChannel),
                    message.editMessage(embed(it.author, text)).asMono().onErrorResume { _ ->
                        it.channel.sendMessage(embed(it.author, text)).asMono()
                    }
                )
            }
    }
}