package team.false_.wtbot.handlers

import club.minnced.jda.reactor.ReactiveEventManager
import reactor.core.Disposable

abstract class Handler {
    protected lateinit var manager: ReactiveEventManager
        private set

    fun inject(manager: ReactiveEventManager) {
        this.manager = manager
    }

    abstract fun subscribe(): Disposable
}