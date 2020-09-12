package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.apache.logging.log4j.LogManager
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.exceptions.AccessDeniedException
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.helpers.*
import java.awt.Color
import java.time.Instant
import javax.inject.Inject

class DelRoleHandler @Inject constructor() : Handler() {
    companion object {
        private val log = LogManager.getLogger()
    }

    override fun subscribe(): Disposable {
        return manager.on<MessageReceivedEvent>()
            .map { it.message }
            .filter { it.channel.isCommandChannel && it.contentRaw.startsWith("!delrole") }
            .flatMap { it ->
                val accessLevel = it.member!!.accessLevel

                if (accessLevel < AccessLevel.JUNIOR)
                    return@flatMap Flux.error(AccessDeniedException(AccessLevel.JUNIOR, accessLevel, it.channel))

                if (it.mentionedMembers.isEmpty() or it.mentionedRoles.isEmpty())
                    return@flatMap Flux.error(EmptyMentionsException(it.channel))

                val allowedRoles = it.mentionedRoles.filter { accessLevel >= it.requiredAccessLevel }
                val deniedRoles = it.mentionedRoles.filter { accessLevel < it.requiredAccessLevel }

                val sUsers = it.mentionedUsers.joinToString(", ") { it.asMention }
                val sAllowed = allowedRoles.joinToString(", ") { it.asMention }
                val sDenied = deniedRoles.joinToString(", ") { it.asMention }

                val text = ArrayList<String>(3)
                    .apply { add("Users: $sUsers") }
                    .apply { if (sAllowed.isNotEmpty()) add("Removed Roles: $sAllowed") }
                    .apply { if (sDenied.isNotEmpty()) add("Not Removed Roles: $sDenied") }
                    .joinToString("\n")

                val pDelRole = it.mentionedMembers.toFlux().flatMap { member ->
                    allowedRoles.toFlux().flatMap(member::delRole)
                }

                val pAnswer = it.channel.sendMessage(
                    EmbedBuilder()
                        .setColor(Color(0, 200, 0))
                        .setTitle("Success")
                        .setDescription(text)
                        .build()
                ).asMono()

                val pLogStaff = it.jda.staffLogs.sendMessage(
                    EmbedBuilder()
                        .setTitle("Del Role")
                        .setDescription(text)
                        .setFooter(it.author.asTag, it.author.avatarUrl)
                        .setTimestamp(Instant.now())
                        .build()
                ).asMono()

                log.info("[<@${it.author.id}>] Users: $sUsers; Allowed: $sAllowed; Denied: $sDenied;")

                Flux.concat(pDelRole, pAnswer, pLogStaff)
            }
            .subscribeDefault()
    }
}