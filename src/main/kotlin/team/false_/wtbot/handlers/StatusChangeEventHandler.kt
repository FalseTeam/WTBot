package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.StatusChangeEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Colors
import team.false_.wtbot.utils.logOutput
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class StatusChangeEventHandler(manager: ReactiveEventManager) : Handler(manager) {
    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<StatusChangeEvent> { event ->
            Flux.just(event).flatMap {
                if (it.newStatus == JDA.Status.CONNECTED && it.oldStatus == JDA.Status.LOADING_SUBSYSTEMS)
                    it.jda.logOutput(footer = "Lifecycle", title = "Started", color = Colors.SUCCESS).asMono()
                else if (it.newStatus == JDA.Status.SHUTTING_DOWN && it.oldStatus == JDA.Status.CONNECTED)
                    it.jda.logOutput(footer = "Lifecycle", title = "Shutdown", color = Colors.WARN).asMono()
                else Flux.empty()
            }
        }
    }
}
