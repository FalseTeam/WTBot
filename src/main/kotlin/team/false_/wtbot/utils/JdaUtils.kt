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
val JDA.logVoice get() = this.getTextChannelById(Channels.LOG_VOICE)!!
val JDA.logStaff get() = this.getTextChannelById(Channels.LOG_STAFF)!!
val JDA.devInput get() = this.getTextChannelById(Channels.DEV_INPUT)!!
val JDA.devOutput get() = this.getTextChannelById(Channels.DEV_OUTPUT)!!
val JDA.devError get() = this.getTextChannelById(Channels.DEV_ERROR)!!

fun JDA.logVoice(
    subject: User? = null,
    title: String,
    description: String,
    color: Int? = null,
    temporal: TemporalAccessor = Instant.now()
): MessageAction {
    return this.logVoice.sendMessage(
        EmbedBuilder()
            .apply { color?.let(this::setColor) }
            .setTitle(title)
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
    return this.logStaff.sendMessage(
        EmbedBuilder()
            .apply { color?.let(this::setColor) }
            .setTitle(title)
            .setDescription(description)
            .setFooter(subject.asTag, subject.avatarUrl)
            .setTimestamp(temporal)
            .build()
    )
}

fun JDA.logOutput(
    title: String? = null,
    description: String? = null,
    footer: String? = null,
    color: Int? = null,
    timestamp: Boolean = false
): MessageAction {
    return this.devOutput.sendMessage(
        EmbedBuilder()
            .apply { color?.let(this::setColor) }
            .apply { title?.let(this::setTitle) }
            .apply { description?.let(this::setDescription) }
            .apply { footer?.let(this::setFooter) }
            .apply { if (timestamp) this.setTimestamp(Instant.now()) }
            .build()
    )
}

fun JDA.logWarn(description: String, footer: String? = null): MessageAction {
    return this.devError.sendMessage(
        EmbedBuilder()
            .setColor(Colors.WARN)
            .setTitle("WARN")
            .setDescription(description)
            .apply { footer?.let(this::setFooter) }
            .setTimestamp(Instant.now())
            .build()
    )
}

fun JDA.logError(e: Throwable): MessageAction {
    return this.devError.sendMessage(
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

fun MessageChannel.sendSuccess(title: String, text: String? = null): MessageAction {
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
            .setColor(Colors.WARN)
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

fun List<IMentionable>.joinCommaSpace() = this.joinToString(", ") { it.asMention }
fun List<IMentionable>.joinNewLine() = this.joinToString("\n") { it.asMention }
