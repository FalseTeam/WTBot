package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
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

class GuildMemberRoleAddEventHandler(manager: ReactiveEventManager) : Handler(manager) {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<GuildMemberRoleAddEvent> { event ->
            Flux.just(event).flatMap {
                val entry = it.guild.retrieveAuditLogs().type(ActionType.MEMBER_ROLE_UPDATE)
                    .firstOrNull { e -> e.targetIdLong == it.member.idLong }
                when {
                    entry == null -> {
                        log.info("[Role Add Without Audit Log] ${it.user.asMention} - ${it.roles.joinCommaSpace()}")
                        it.jda.logStaff(
                            title = "Role Add Without Audit Log",
                            description = "${it.member.asMention} ${it.roles.joinCommaSpace()}",
                            color = Colors.ROLE_ADD
                        ).asMono()
                    }
                    entry.user!!.idLong == it.jda.selfUser.idLong -> Mono.empty()
                    else -> {
                        log.info("[Role Add Manual] ${entry.user!!.asMention} - [User] ${it.member.asMention} - [Roles] ${it.roles.joinCommaSpace()}")
                        it.jda.logStaff(
                            subject = entry.user!!,
                            title = "Role Add Manual",
                            description = "${it.member.asMention} ${it.roles.joinCommaSpace()}",
                            color = Colors.ROLE_ADD
                        ).asMono()
                    }
                }
            }
        }
    }
}
