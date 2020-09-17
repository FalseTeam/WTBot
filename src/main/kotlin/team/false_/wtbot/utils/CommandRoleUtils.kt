package team.false_.wtbot.utils

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.log

fun addRole(
    subject: User,
    channel: MessageChannel,
    members: List<Member>,
    rAllowed: List<Role>,
    rDenied: List<Role>
): Flux<out Any> {
    val sUsers = members.joinDefault()
    val sAllowed = rAllowed.joinDefault()
    val sDenied = rDenied.joinDefault()

    val text = ArrayList<String>(3)
        .apply { add("Users: $sUsers") }
        .apply { if (sAllowed.isNotEmpty()) add("Granted Roles: $sAllowed") }
        .apply { if (sDenied.isNotEmpty()) add("Not Granted Roles: $sDenied") }
        .joinToString("\n")

    log.info("[AddRole] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

    return Flux.concat(
        members.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::addRole) },
        channel.sendSuccess(text).asMono(),
        channel.jda.logStaff(subject, "Add Role", text, Colors.ROLE_ADD).asMono()
    )
}

fun removeRole(
    subject: User,
    channel: MessageChannel,
    members: List<Member>,
    rAllowed: List<Role>,
    rDenied: List<Role>
): Flux<out Any> {
    val sUsers = members.joinDefault()
    val sAllowed = rAllowed.joinDefault()
    val sDenied = rDenied.joinDefault()

    val text = ArrayList<String>(3)
        .apply { add("Users: $sUsers") }
        .apply { if (sAllowed.isNotEmpty()) add("Removed Roles: $sAllowed") }
        .apply { if (sDenied.isNotEmpty()) add("Not Removed Roles: $sDenied") }
        .joinToString("\n")

    log.info("[DelRole] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

    return Flux.concat(
        members.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::removeRole) },
        channel.sendSuccess(text).asMono(),
        channel.jda.logStaff(subject, "Del Role", text, Colors.ROLE_DEL).asMono()
    )
}