package team.false_.wtbot

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import team.false_.wtbot.handlers.Handler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Worker @Inject constructor(handlers: Set<@JvmSuppressWildcards Handler>) {
    private val manager: ReactiveEventManager = ReactiveEventManager()
    val jda: JDA

    init {
        handlers.forEach {
            it.inject(manager)
            it.subscribe()
        }
        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
            .setEventManager(manager)
            .build()
    }
}