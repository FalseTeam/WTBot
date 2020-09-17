package team.false_.wtbot

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import team.false_.wtbot.handlers.MessageReceivedEventHandler
import team.false_.wtbot.handlers.ReadyEventHandler
import team.false_.wtbot.handlers.VerificationHandler
import kotlin.reflect.full.createInstance

class Worker constructor(token: String) {
    private val jda: JDA

    init {
        val manager = ReactiveEventManager()

        setOf(
            ReadyEventHandler::class,
            VerificationHandler::class,
            MessageReceivedEventHandler::class,
        ).forEach { it.createInstance().inject(manager).subscribe() }

        jda = JDABuilder.createDefault(token)
            .setEventManager(manager)
            .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
            .build()
    }

    fun shutdown() {
        jda.shutdown()
    }
}