package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import reactor.core.Disposable

abstract class Handler(protected val manager: ReactiveEventManager) {
    abstract fun subscribe(): Disposable

    open fun onReady(jda: JDA) {}
}
