package team.false_.wtbot

import org.apache.logging.log4j.LogManager

object Main {
    internal val log = LogManager.getLogger()

    @JvmStatic
    fun main(args: Array<String>) {
        val worker = Worker(System.getenv("TOKEN"))

        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Interrupt Signal Received")
            worker.shutdown()
        })
    }
}
