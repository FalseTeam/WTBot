package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import team.false_.wtbot.config.Colors
import team.false_.wtbot.log
import team.false_.wtbot.utils.joinDefault
import team.false_.wtbot.utils.logStaff
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError
import java.time.Instant

class GuildMemberRoleRemoveEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildMemberRoleRemoveEvent> { event ->
            Flux.just(event).flatMap {
                val now = Instant.now()
                val entry = it.guild.retrieveAuditLogs().type(ActionType.MEMBER_ROLE_UPDATE)
                    .first { e -> e.targetIdLong == it.member.idLong }
                if (entry.user!!.isBot)
                    Mono.empty()
                else {
                    log.info("[Role Remove Manual] ${entry.user!!.asMention} - [User] ${it.member.asMention} - [Roles] ${it.roles.joinDefault()}")
                    it.jda.logStaff(
                        entry.user!!, "Role Remove Manual", "${it.member.asMention} ${it.roles.joinDefault()}",
                        Colors.ROLE_REMOVE, now
                    ).asMono()
                }
            }
        }
    }
}