package team.false_.wtbot

import club.minnced.jda.reactor.createManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import reactor.core.scheduler.Schedulers
import team.false_.wtbot.handlers.*
import team.false_.wtbot.utils.createInstance
import team.false_.wtbot.utils.doesOverride
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import kotlin.concurrent.thread

class Worker constructor(token: String) {
    private val jda: JDA

    init {
        var count = 0
        val executor = Executors.newScheduledThreadPool(ForkJoinPool.getCommonPoolParallelism()) {
            thread(start = false, block = it::run, name = "jda-thread-${count++}", isDaemon = false)
        }
        val manager = createManager {
            scheduler = Schedulers.fromExecutor(executor)
            isDispose = true
        }

        val handlers = listOf(
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
            StatusChangeEventHandler::class,
        ).map { it.createInstance(manager) }

        handlers.filterNot { it::class.doesOverride(it::onReady) }.forEach(Handler::subscribe)

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
        handlers.filter { it::class.doesOverride(it::onReady) }.forEach(Handler::subscribe)
    }
}
