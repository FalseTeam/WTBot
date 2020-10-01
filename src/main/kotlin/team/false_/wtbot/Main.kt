@file:JvmName("Main")

package team.false_.wtbot

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val log: Logger = LogManager.getLogger()

fun main() {
    Worker(System.getenv("TOKEN"))
    Runtime.getRuntime().addShutdownHook(Thread { log.info("Interrupt Signal Received") })
}
