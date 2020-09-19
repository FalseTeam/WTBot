package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.utils.logVoice
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class GuildVoiceJoinEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildVoiceJoinEvent> { event ->
            Flux.just(event).flatMap {
                it.jda.logVoice(
                    title = "Voice Join",
                    description = "${it.member.asMention} ${it.newValue.name}",
                    color = Colors.VOICE_JOIN
                ).asMono()
            }
        }
    }
}