package team.false_.wtbot.helpers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.*
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import reactor.util.function.*
import team.false_.wtbot.Main
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.exceptions.AccessDeniedException
import team.false_.wtbot.exceptions.EmptyMentionsException
import java.awt.Color

internal fun Flux<Message>.filterCommand(command: String): Flux<Message> {
    return this.filter {
        it.channel.isCommandChannel &&
                it.contentRaw.isNotEmpty() &&
                with(it.contentRaw[0]) { this == '!' || this == '.' } &&
                it.contentRaw.startsWith(command, 1)
    }
}

internal fun Flux<Message>.requireAccessLevel(requiredAccessLevel: AccessLevel): Flux<Message> {
    return this.map {
        val currentAccessLevel = it.member!!.accessLevel
        if (currentAccessLevel < requiredAccessLevel)
            throw AccessDeniedException.require(requiredAccessLevel, currentAccessLevel)
        it
    }
}

internal fun Flux<Message>.requireMembersAndRolesMentions(): Flux<Message> {
    return this.map {
        if (it.mentionedMembers.isEmpty() or it.mentionedRoles.isEmpty())
            throw EmptyMentionsException.default
        it
    }
}

internal fun Flux<Message>.requireMembersMentions(): Flux<Message> {
    return this.map {
        if (it.mentionedMembers.isEmpty())
            throw EmptyMentionsException.members
        it
    }
}

internal fun Flux<Tuple5<User, MessageChannel, List<Member>, List<Role>, List<Role>>>.addRoles(): Flux<Any> {
    return this.flatMap {
        val (subject, channel, members, rAllowed, rDenied) = it

        val sUsers = members.joinDefault()
        val sAllowed = rAllowed.joinDefault()
        val sDenied = rDenied.joinDefault()

        val text = ArrayList<String>(3)
            .apply { add("Users: $sUsers") }
            .apply { if (sAllowed.isNotEmpty()) add("Granted Roles: $sAllowed") }
            .apply { if (sDenied.isNotEmpty()) add("Not Granted Roles: $sDenied") }
            .joinToString("\n")

        Main.log.info("[AddRole] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

        Flux.concat(
            members.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::addRole) },
            channel.sendSuccess(text).asMono(),
            channel.jda.logStaff(subject, "Add Role", text, Color(0, 200, 200)).asMono()
        )
    }
}

internal fun Flux<Tuple5<User, MessageChannel, List<Member>, List<Role>, List<Role>>>.removeRoles(): Flux<Any> {
    return this.flatMap {
        val (subject, channel, members, rAllowed, rDenied) = it

        val sUsers = members.joinDefault()
        val sAllowed = rAllowed.joinDefault()
        val sDenied = rDenied.joinDefault()

        val text = ArrayList<String>(3)
            .apply { add("Users: $sUsers") }
            .apply { if (sAllowed.isNotEmpty()) add("Removed Roles: $sAllowed") }
            .apply { if (sDenied.isNotEmpty()) add("Not Removed Roles: $sDenied") }
            .joinToString("\n")

        Main.log.info("[DelRole] ${subject.asMention} - [Users] $sUsers - [Allowed] $sAllowed - [Denied] $sDenied")

        Flux.concat(
            members.toFlux().flatMap { member -> rAllowed.toFlux().flatMap(member::removeRole) },
            channel.sendSuccess(text).asMono(),
            channel.jda.logStaff(subject, "Del Role", text, Color(200, 55, 0)).asMono()
        )
    }
}