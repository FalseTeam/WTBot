package team.false_.wtbot.handlers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.events.ReadyEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.log
import team.false_.wtbot.utils.guild
import team.false_.wtbot.utils.logOutput
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class ReadyEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<ReadyEvent> {
            Flux.just(it).flatMap {
                log.info("Logged in as ${it.jda.selfUser.asTag}")
                it.jda.guild.loadMembers().onSuccess { log.info("Loaded Members") }
                it.jda.logOutput(footer = "Lifecycle", title = "Started", color = Colors.SUCCESS).asMono()
            }
        }
    }
}