package team.false_.wtbot.helpers

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.requests.restaction.MessageAction
import team.false_.wtbot.config.Channels
import java.awt.Color
import java.time.Instant

val JDA.staffLogs get() = this.getTextChannelById(Channels.STAFF_LOGS)!!
val JDA.voiceLogs get() = this.getTextChannelById(Channels.VOICE_CHANNELS)!!
val JDA.textLogs get() = this.getTextChannelById(Channels.TEXT_CHANNELS)!!
val JDA.devLogs get() = this.getTextChannelById(Channels.EXCEPTION_LOGS)!!

fun JDA.logStaff(subject: User, title: String, description: String, color: Color? = null): MessageAction {
    return this.staffLogs.sendMessage(
        EmbedBuilder()
            .setTitle(title)
            .setColor(color)
            .setDescription(description)
            .setFooter(subject.asTag, subject.avatarUrl)
            .setTimestamp(Instant.now())
            .build()
    )
}

fun JDA.logWarning(e: Throwable): MessageAction {
    return this.devLogs.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 200, 0))
            .setTitle("WARNING")
            .setDescription(e.localizedMessage)
            .setFooter(e.javaClass.simpleName)
            .setTimestamp(Instant.now())
            .build()
    )
}

fun JDA.logError(e: Throwable): MessageAction {
    return this.devLogs.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 0, 0))
            .setTitle("ERROR")
            .setDescription(e.localizedMessage)
            .setFooter(e.javaClass.simpleName)
            .setTimestamp(Instant.now())
            .build()
    )
}