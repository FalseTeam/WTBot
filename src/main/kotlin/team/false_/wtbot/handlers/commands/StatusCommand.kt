package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Mono
import team.false_.wtbot.exceptions.IllegalArgumentException
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import team.false_.wtbot.utils.sendSuccess

class StatusCommand : AdminCommand() {
    override fun exec(it: Message): Mono<Message> {
        return Mono.just(it)
            .map {
                val split = it.contentRaw.split(" ")
                val status = OnlineStatus.fromKey(split.getOrNull(1))
                if (status == OnlineStatus.UNKNOWN) throw IllegalArgumentException("Enter Status: online/idle/dnd/invisible/offline")
                status
            }
            .doOnNext(it.jda.presence::setStatus)
            .flatMap { s -> it.channel.sendSuccess("Status Set", s.toString()).asMono() }
    }
}