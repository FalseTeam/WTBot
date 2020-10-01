package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.utils.logVoice
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError
import java.time.Instant

class GuildVoiceGuildMuteEventHandler(manager: ReactiveEventManager) : Handler(manager) {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildVoiceGuildMuteEvent> { event ->
            Flux.just(event).flatMap {
                val now = Instant.now()
                val entry = it.guild.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE)
                    .first { e -> e.targetIdLong == it.member.idLong }
                val title: String
                val color: Int
                if (it.isGuildMuted) {
                    title = "Voice Guild Mute"
                    color = Colors.VOICE_GUILD_MUTE
                } else {
                    title = "Voice Guild UnMute"
                    color = Colors.VOICE_GUILD_UNMUTE
                }
                it.jda.logVoice(
                    entry.user, title, "${it.member.asMention} ${it.voiceState.channel!!.name}", color, now
                ).asMono()
            }
        }
    }
}
