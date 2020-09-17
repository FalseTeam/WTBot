package team.false_.wtbot.utils

import club.minnced.jda.reactor.ReactiveEventManager
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.exceptions.WTBotException
import team.false_.wtbot.log

inline fun <reified T : GenericEvent> ReactiveEventManager.subscribeOnAnyWithHandleError(noinline handler: (T) -> Flux<Any>): Disposable {
    return this.onWithHandleError(T::class.java, handler).subscribe()
}

inline fun <reified T : GenericEvent> ReactiveEventManager.onAnyWithHandleError(noinline handler: (T) -> Flux<Any>): Flux<Any> {
    return this.onWithHandleError(T::class.java, handler)
}

inline fun <reified T : GenericEvent, R> ReactiveEventManager.onWithHandleError(noinline handler: (T) -> Flux<R>): Flux<R> {
    return this.onWithHandleError(T::class.java, handler)
}

fun <T : GenericEvent, R> ReactiveEventManager.onWithHandleError(clazz: Class<T>, handler: (T) -> Flux<R>): Flux<R> {
    return this.on(clazz).flatMap {
        handler(it).onErrorResume { e ->
            if (it is MessageReceivedEvent) {
                if (e is WTBotException) {
                    log.warn("[${e.javaClass.simpleName}] ${it.message.author.asMention} - ${it.message.contentRaw}")
                    it.message.channel.sendWarning(e).submit()
                } else {
                    log.error(
                        "[${e.javaClass.simpleName}] ${it.message.author.asMention} - ${it.message.contentRaw}", e
                    )
                    it.message.channel.sendInternalError().submit()
                    it.jda.logError(e).submit()
                }
            } else {
                log.error("[${e.javaClass.simpleName}] $it", e)
                it.jda.logError(e).submit()
            }
            Flux.empty()
        }
    }
}