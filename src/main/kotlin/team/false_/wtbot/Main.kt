package team.false_.wtbot

import org.apache.logging.log4j.LogManager
import team.false_.wtbot.components.WorkerFactory

object Main {
    internal val log = LogManager.getLogger()
    lateinit var worker: Worker
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        worker = WorkerFactory.create().worker()

        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Interrupt Signal Received")
            worker.jda.shutdown()
        })
    }
}
