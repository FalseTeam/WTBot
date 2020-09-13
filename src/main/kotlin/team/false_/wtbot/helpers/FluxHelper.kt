package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.exceptions.AccessDeniedException
import team.false_.wtbot.exceptions.EmptyMentionsException

internal fun Flux<Message>.filterCommand(command: String) =
    this.filter { it.channel.isCommandChannel && it.contentRaw.startsWith("!$command") }

internal fun Flux<Message>.requireAccessLevel(requiredAccessLevel: AccessLevel) =
    this.map {
        val currentAccessLevel = it.member!!.accessLevel
        if (currentAccessLevel < requiredAccessLevel)
            throw AccessDeniedException.require(requiredAccessLevel, currentAccessLevel)
        it
    }

internal fun Flux<Message>.requireMembersAndRolesMentions() =
    this.map {
        if (it.mentionedMembers.isEmpty() or it.mentionedRoles.isEmpty())
            throw EmptyMentionsException.default
        it
    }