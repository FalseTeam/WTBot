package team.false_.wtbot.helpers

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.requests.restaction.MessageAction
import team.false_.wtbot.Config
import java.awt.Color
import java.time.Instant

val JDA.guild get() = this.getGuildById(Config.GUILD_ID)!!
val JDA.staffLogs get() = this.getTextChannelById(Config.CHANNEL_LOG_STAFF_ID)!!
val JDA.voiceLogs get() = this.getTextChannelById(Config.CHANNEL_LOG_VOICE_ID)!!
val JDA.textLogs get() = this.getTextChannelById(Config.CHANNEL_LOG_TEXT_ID)!!
val JDA.staffChannel get() = this.getTextChannelById(Config.CHANNEL_STAFF_ID)!!
val JDA.exceptionsChannel get() = this.getTextChannelById(Config.CHANNEL_LOG_EXCEPTION_ID)!!

fun JDA.logStaff(subject: User, title: String, description: String): MessageAction {
    return this.staffLogs.sendMessage(
        EmbedBuilder()
            .setTitle(title)
            .setDescription(description)
            .setFooter(subject.asTag, subject.avatarUrl)
            .setTimestamp(Instant.now())
            .build()
    )
}

fun JDA.logWarning(e: Throwable): MessageAction {
    return this.exceptionsChannel.sendMessage(
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
    return this.exceptionsChannel.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 0, 0))
            .setTitle("ERROR")
            .setDescription(e.localizedMessage)
            .setFooter(e.javaClass.simpleName)
            .setTimestamp(Instant.now())
            .build()
    )
}