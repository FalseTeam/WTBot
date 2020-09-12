package team.false_.wtbot.helpers

import net.dv8tion.jda.api.EmbedBuilder
import reactor.core.publisher.Flux
import team.false_.wtbot.Main
import team.false_.wtbot.exceptions.CallbackException
import java.awt.Color
import java.time.Instant

internal fun <T> Flux<T>.retryWhenCallbackException() = this.retryWhen {
    it.map {
        if (it is CallbackException) {
            it.channel.sendMessage(
                EmbedBuilder()
                    .setColor(Color(200, 200, 0))
                    .setTitle("Error")
                    .setDescription(it.localizedMessage)
                    .build()
            ).submit()
            it
        } else throw it
    }
}

internal fun <T> Flux<T>.logOnError() = this.doOnError {
    Main.log.fatal("", it)
    Main.worker.jda.exceptionsChannel.sendMessage(
        EmbedBuilder()
            .setColor(Color(200, 0, 0))
            .setTitle("FATAL")
            .setDescription(it.localizedMessage)
            .setFooter(it.javaClass.simpleName)
            .setTimestamp(Instant.now())
            .build()
    ).submit()
}

internal fun <T> Flux<T>.subscribeDefault() = this.retryWhenCallbackException().logOnError().subscribe()
