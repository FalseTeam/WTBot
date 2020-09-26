package team.false_.wtbot

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import team.false_.wtbot.config.Colors
import team.false_.wtbot.handlers.*
import team.false_.wtbot.utils.logOutput
import kotlin.reflect.full.createInstance

class Worker constructor(token: String) {
    private val jda: JDA

    init {
        val manager = ReactiveEventManager()

        val handlers = setOf(
            ReadyEventHandler::class,
            VerificationHandler::class,
            MessageReceivedEventHandler::class,
            GuildMemberRoleAddEventHandler::class,
            GuildMemberRoleRemoveEventHandler::class,
            GuildVoiceJoinEventHandler::class,
            GuildVoiceMoveEventHandler::class,
            GuildVoiceLeaveEventHandler::class,
            GuildVoiceGuildMuteEventHandler::class,
            GuildVoiceGuildDeafenEventHandler::class,
        ).map { it.createInstance() }

        handlers.forEach { it.inject(manager) }

        jda = JDABuilder.createDefault(token)
            .setEventManager(manager)
            .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .enableCache(CacheFlag.MEMBER_OVERRIDES)
            .enableCache(CacheFlag.VOICE_STATE)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()

        jda.awaitReady()
        handlers.forEach { it.onReady(jda) }
        handlers.forEach { it.subscribe() }
        jda.logOutput(footer = "Lifecycle", title = "Started", color = Colors.SUCCESS).submit()
    }

    fun shutdown() {
        jda.logOutput(footer = "Lifecycle", title = "Shutdown", color = Colors.WARN).submit()
        jda.shutdown()
    }
}