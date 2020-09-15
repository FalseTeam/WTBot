package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Mono
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import team.false_.wtbot.utils.accessLevel

class AccessLevelCommand : AdminCommand() {
    override fun exec(it: Message): Mono<Message> {
        val text =
            if (it.mentionedMembers.isEmpty()) "${it.member!!.asMention} - ${it.member!!.accessLevel}"
            else it.mentionedMembers.joinToString("\n") { "${it.asMention} - ${it.accessLevel}" }
        return it.channel.sendMessage(
            EmbedBuilder()
                .setTitle("Access Level")
                .setDescription(text)
                .build()
        ).asMono()
    }
}