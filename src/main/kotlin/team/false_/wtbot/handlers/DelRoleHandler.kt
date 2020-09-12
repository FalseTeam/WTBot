package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.apache.logging.log4j.LogManager
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import team.false_.wtbot.entities.AccessLevel
import team.false_.wtbot.helpers.accessLevel
import team.false_.wtbot.helpers.isCommandChannel
import team.false_.wtbot.helpers.requiredAccessLevel
import team.false_.wtbot.helpers.staffLogs
import javax.inject.Inject

class DelRoleHandler @Inject constructor() : Handler() {
    companion object {
        private val log = LogManager.getLogger()
    }

    override fun subscribe(): Disposable {
        return manager.on<MessageReceivedEvent>()
            .map { it.message }
            .filter { it.channel.isCommandChannel && it.contentRaw.startsWith("!delrole") }
            .flatMap { message ->
                val channel = message.channel
                val accessLevel = message.member!!.accessLevel

                if (accessLevel < AccessLevel.JUNIOR)
                    return@flatMap channel
                        .sendMessage("Access Denied. Required Access Level is ${AccessLevel.JUNIOR}. Your Access Level is $accessLevel.")
                        .asMono()

                if (message.mentionedMembers.isEmpty() or message.mentionedRoles.isEmpty())
                    return@flatMap channel.sendMessage("Mentioned Members and Roles are required").asMono()

                val allowedRoles = message.mentionedRoles.filter { accessLevel >= it.requiredAccessLevel }
                val deniedRoles = message.mentionedRoles.filter { accessLevel < it.requiredAccessLevel }

                val sUsers = message.mentionedUsers.joinToString(", ") { "<@${it.id}>" }
                val sAllowed = allowedRoles.joinToString(", ") { "<@&${it.id}>" }
                val sDenied = deniedRoles.joinToString(", ") { "<@&${it.id}>" }

                val text = ArrayList<String>(4)
                    .apply { add("Subject: <@${message.author.id}>") }
                    .apply { add("Users: $sUsers") }
                    .apply { if (sAllowed.isNotEmpty()) add("Removed Roles: $sAllowed") }
                    .apply { if (sDenied.isNotEmpty()) add("Not Removed Roles: $sDenied") }
                    .joinToString("\n")

                val pAnswer = channel.sendMessage(text).asMono()
                val pLogStaff = message.jda.staffLogs.sendMessage(text).asMono()
                val pAddRole = message.mentionedMembers.toFlux().flatMap { member ->
                    allowedRoles.toFlux().flatMap { it.guild.removeRoleFromMember(member, it).asMono() }
                }

                log.info("[<@${message.author.id}>] Users: $sUsers; Allowed: $sAllowed; Denied: $sDenied;")

                Flux.concat(pAnswer, pLogStaff, pAddRole)
            }
            .subscribe({}, { log.error(it) })
    }
}