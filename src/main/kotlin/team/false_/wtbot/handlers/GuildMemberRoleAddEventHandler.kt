package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.audit.ActionType
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import team.false_.wtbot.config.Colors
import team.false_.wtbot.log
import team.false_.wtbot.utils.joinCommaSpace
import team.false_.wtbot.utils.logStaff
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError
import java.time.Instant

class GuildMemberRoleAddEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildMemberRoleAddEvent> { event ->
            Flux.just(event).flatMap {
                val now = Instant.now()
                val entry = it.guild.retrieveAuditLogs().type(ActionType.MEMBER_ROLE_UPDATE)
                    .first { e -> e.targetIdLong == it.member.idLong }
                if (entry.user!!.isBot)
                    Mono.empty()
                else {
                    log.info("[Role Add Manual] ${entry.user!!.asMention} - [User] ${it.member.asMention} - [Roles] ${it.roles.joinCommaSpace()}")
                    it.jda.logStaff(
                        entry.user!!, "Role Add Manual", "${it.member.asMention} ${it.roles.joinCommaSpace()}",
                        Colors.ROLE_ADD, now
                    ).asMono()
                }
            }
        }
    }
}