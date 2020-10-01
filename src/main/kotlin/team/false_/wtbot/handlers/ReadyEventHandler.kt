package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.events.ReadyEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.log
import team.false_.wtbot.utils.guild
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class ReadyEventHandler(manager: ReactiveEventManager) : Handler(manager) {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<ReadyEvent> {
            log.info("Logged in as ${it.jda.selfUser.asTag}")
            it.jda.guild.loadMembers().onSuccess { log.info("Loaded Members") }
            Flux.empty()
        }
    }
}
