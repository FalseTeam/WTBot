package team.false_.wtbot.utils

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.MessageAction
import team.false_.wtbot.config.Channels
import team.false_.wtbot.config.Colors
import team.false_.wtbot.config.Config
import team.false_.wtbot.exceptions.WTBotException
import java.time.Instant
import java.time.temporal.TemporalAccessor

/*
JDA Class
 */

val JDA.guild get() = this.getGuildById(Config.GUILD)!!
val JDA.voiceLogs get() = this.getTextChannelById(Channels.VOICE_CHANNELS)!!
val JDA.staffLogs get() = this.getTextChannelById(Channels.STAFF_LOGS)!!
val JDA.devLogs get() = this.getTextChannelById(Channels.NULL)!!

fun JDA.logVoice(
    subject: User? = null,
    title: String,
    description: String,
    color: Int? = null,
    temporal: TemporalAccessor = Instant.now()
): MessageAction {
    return this.voiceLogs.sendMessage(
        EmbedBuilder()
            .setTitle(title)
            .apply { color?.let(this::setColor) }
            .setDescription(description)
            .apply { subject?.let { setFooter(it.asTag, it.avatarUrl) } }
            .setTimestamp(temporal)
            .build()
    )
}

fun JDA.logStaff(
    subject: User,
    title: String,
    description: String,
    color: Int? = null,
    temporal: TemporalAccessor = Instant.now()
): MessageAction {
    return this.staffLogs.sendMessage(
        EmbedBuilder()
            .setTitle(title)
            .apply { color?.let(this::setColor) }
            .setDescription(description)
            .setFooter(subject.asTag, subject.avatarUrl)
            .setTimestamp(temporal)
            .build()
    )
}

fun JDA.logError(e: Throwable): MessageAction {
    return this.devLogs.sendMessage(
        EmbedBuilder()
            .setColor(Colors.ERROR)
            .setTitle("ERROR")
            .setDescription(e.localizedMessage)
            .setFooter(e.javaClass.simpleName)
            .setTimestamp(Instant.now())
            .build()
    )
}

/*
Member Class
 */

val Member.accessLevel get() = this.roles.map { it.idLong }.map(Config::accessLevel).toSet().maxOf { it }

fun Member.addRole(role: Role) = this.guild.addRoleToMember(this, role).asMono()
fun Member.removeRole(role: Role) = this.guild.removeRoleFromMember(this, role).asMono()

/*
Message Class
 */

val Message.mentionedRolesAllowed
    get() = this.mentionedRoles.filter { this.member!!.accessLevel >= it.requiredAccessLevel }
val Message.mentionedRolesIgnored
    get() = this.mentionedRoles.filter { this.member!!.accessLevel < it.requiredAccessLevel }

/*
Role Class
 */

val Role.requiredAccessLevel get() = Config.requiredAccessLevel(this.idLong)

/*
MessageChannel Class
 */

fun MessageChannel.sendSuccess(title: String, text: String): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Colors.SUCCESS)
            .setTitle(title)
            .setDescription(text)
            .build()
    )
}

fun MessageChannel.sendWarning(e: WTBotException): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Colors.WARNING)
            .setTitle(e.title)
            .setDescription(e.localizedMessage)
            .build()
    )
}

fun MessageChannel.sendInternalError(): MessageAction {
    return this.sendMessage(
        EmbedBuilder()
            .setColor(Colors.ERROR)
            .setDescription("Internal Error")
            .build()
    )
}

/*
List Class
 */

fun List<IMentionable>.joinDefault() = this.joinToString(", ") { it.asMention }
