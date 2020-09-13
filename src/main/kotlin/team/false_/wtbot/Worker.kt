package team.false_.wtbot

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import team.false_.wtbot.handlers.AccessLevelHandler
import team.false_.wtbot.handlers.ReadyEventHandler
import team.false_.wtbot.handlers.VerificationHandler
import team.false_.wtbot.handlers.roles.*
import kotlin.reflect.full.createInstance

class Worker constructor(token: String) {
    private val jda: JDA

    init {
        val manager = ReactiveEventManager()

        setOf(
            ReadyEventHandler::class,
            VerificationHandler::class,
            AccessLevelHandler::class,

            AddRoleHandler::class,
            DelRoleHandler::class,
            ChatBanHandler::class,
            ChatUnBanHandler::class,
            VoiceBanHandler::class,
            VoiceUnBanHandler::class,
            ChatVoiceBanHandler::class,
            ChatVoiceUnBanHandler::class
        ).forEach { it.createInstance().inject(manager).subscribe() }

        jda = JDABuilder.createDefault(token)
            .setEventManager(manager)
            .build()
    }

    fun shutdown() {
        jda.shutdown()
    }
}