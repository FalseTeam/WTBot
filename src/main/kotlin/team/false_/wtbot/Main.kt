@file:JvmName("Main")

package team.false_.wtbot

import org.apache.logging.log4j.LogManager

internal val log = LogManager.getLogger()

fun main() {
    val worker = Worker(System.getenv("TOKEN"))

    Runtime.getRuntime().addShutdownHook(Thread {
        log.info("Interrupt Signal Received")
        worker.shutdown()
    })
}