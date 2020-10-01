package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.audit.AuditLogEntry
import net.dv8tion.jda.api.audit.AuditLogOption
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.utils.TimeUtil
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.utils.guild
import team.false_.wtbot.utils.logVoice
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError
import java.time.Instant

class GuildVoiceLeaveEventHandler(manager: ReactiveEventManager) : Handler(manager) {
    private var entries = ArrayList<AuditLogEntry>()

    override fun onReady(jda: JDA) {
        val nowDs = System.currentTimeMillis() - TimeUtil.DISCORD_EPOCH - 300_000
        entries.addAll(
            jda.guild.retrieveAuditLogs()
                .type(ActionType.MEMBER_VOICE_KICK).cache(false).complete()
                .filter { e -> e.idLong ushr TimeUtil.TIMESTAMP_OFFSET.toInt() >= nowDs }
        )
    }

    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildVoiceLeaveEvent> { event ->
            Flux.just(event).flatMap {
                val now = System.currentTimeMillis()
                val nowDs = now - TimeUtil.DISCORD_EPOCH - 300_000
                // voice move audit entry lives 5 minutes (calculated via experiments) (voice kick audit entry not checked)
                val filterDead: (e: AuditLogEntry) -> Boolean =
                    { e -> e.idLong ushr TimeUtil.TIMESTAMP_OFFSET.toInt() < nowDs }
                entries.removeIf(filterDead)

                val entriesOld = entries

                val complete = it.guild.retrieveAuditLogs().type(ActionType.MEMBER_VOICE_KICK).cache(false).complete()

                val entry = complete.subList(0, complete.indexOfFirst(filterDead)).firstOrNull { e ->
                    !entriesOld.contains(e) || entriesOld.first { o -> o == e }
                        .getOption<String>(AuditLogOption.COUNT) != e.getOption<String>(AuditLogOption.COUNT)
                }

                val subject: User?
                val title: String

                if (entry == null) {
                    subject = null
                    title = "Voice Leave"
                } else {
                    entries.remove(entry)
                    entries.add(entry)
                    subject = entry.user
                    title = "Voice Kick"
                }

                it.jda.logVoice(
                    subject, title, "${it.member.asMention} ${it.oldValue.name}",
                    Colors.VOICE_LEAVE, Instant.ofEpochMilli(now)
                ).asMono()
            }
        }
    }
}
