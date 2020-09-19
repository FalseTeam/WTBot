package team.false_.wtbot.handlers.commands

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.internal.entities.EntityBuilder
import reactor.core.publisher.Mono
import team.false_.wtbot.exceptions.IllegalArgumentException
import team.false_.wtbot.handlers.commands.abstract.AdminCommand
import team.false_.wtbot.utils.sendSuccess

class ActivityCommand : AdminCommand() {
    override fun exec(it: Message): Mono<Message> {
        return Mono.just(it)
            .map {
                val split = it.contentRaw.split(" ")
                val type = when (split.getOrNull(1)) {
                    "playing" -> Activity.ActivityType.DEFAULT
                    "streaming" -> Activity.ActivityType.STREAMING
                    "listening" -> Activity.ActivityType.LISTENING
                    "watching" -> Activity.ActivityType.WATCHING
                    "custom" -> Activity.ActivityType.CUSTOM_STATUS
                    else -> throw IllegalArgumentException("Enter Type: playing/streaming/listening/watching")
                }
                if (split.size < 3) throw IllegalArgumentException("Enter Name")
                val name: String
                val url: String?
                if (type == Activity.ActivityType.STREAMING) {
                    if (split.size < 4) throw IllegalArgumentException("Enter URL")
                    name = split.subList(2, split.size - 1).joinToString(" ")
                    url = split.last()
                } else {
                    name = split.subList(2, split.size).joinToString(" ")
                    url = null
                }
                EntityBuilder.createActivity(name, url, type)
            }
            .doOnNext(it.jda.presence::setActivity)
            .flatMap { a -> it.channel.sendSuccess("Activity Set", "${a.type.name} ${a.name} ${a.url}").asMono() }
    }
}