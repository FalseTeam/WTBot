package team.false_.wtbot.helpers

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction
import team.false_.wtbot.config.Config
import java.awt.Color

val MessageChannel.isCommandChannel get() = this.idLong == Config.COMMAND_CHANNEL

fun MessageChannel.sendSuccess(text: String): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Color(0, 200, 0))
            .setDescription(text)
            .build()
    )
}

fun MessageChannel.sendWarning(e: Throwable): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 200, 0))
            .setDescription(e.localizedMessage)
            .build()
    )
}

fun MessageChannel.sendInternalError(): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 0, 0))
            .setDescription("Internal Error")
            .build()
    )
}