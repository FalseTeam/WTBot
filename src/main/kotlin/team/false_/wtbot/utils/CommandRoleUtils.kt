package team.false_.wtbot.utils

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.log

fun addRole(
    subject: User, channel: MessageChannel, members: List<Member>,
    rAllowed: List<Role>, rIgnored: List<Role>
) = roleAction(subject, channel, members, rAllowed, rIgnored, "Role Add", Colors.ROLE_ADD) {
    rAllowed.toFlux().flatMap(it::addRole)
}

fun removeRole(
    subject: User, channel: MessageChannel, members: List<Member>,
    rAllowed: List<Role>, rIgnored: List<Role>
) = roleAction(subject, channel, members, rAllowed, rIgnored, "Role Remove", Colors.ROLE_REMOVE) {
    rAllowed.toFlux().flatMap(it::removeRole)
}

fun roleAction(
    subject: User,
    channel: MessageChannel,
    members: List<Member>,
    rAllowed: List<Role>,
    rIgnored: List<Role>,
    title: String,
    color: Int,
    actionWithMember: (Member) -> Publisher<out Any>
): Flux<out Any> {
    val sUsers = members.joinCommaSpace()
    val sAllowed = rAllowed.joinCommaSpace()
    val sIgnored = rIgnored.joinCommaSpace()

    val text = ArrayList<String>(3)
        .apply { add("Users: $sUsers") }
        .apply { if (sAllowed.isNotEmpty()) add("Roles: $sAllowed") }
        .apply { if (sIgnored.isNotEmpty()) add("Ignored Roles: $sIgnored") }
        .joinToString("\n")

    log.info("[${title}] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Ignored] $sIgnored")

    return Flux.concat(
        members.toFlux().flatMap(actionWithMember),
        channel.sendSuccess(title, text).asMono(),
        channel.jda.logStaff(subject, title, text, color).asMono()
    )
}