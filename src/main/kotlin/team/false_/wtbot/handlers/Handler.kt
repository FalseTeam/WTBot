package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import reactor.core.Disposable

abstract class Handler {
    protected lateinit var manager: ReactiveEventManager
        private set

    fun inject(manager: ReactiveEventManager): Handler {
        this.manager = manager
        return this
    }

    abstract fun subscribe(): Disposable

    open fun onReady(jda: JDA) {}
}