package team.false_.wtbot

import org.apache.logging.log4j.LogManager
import team.false_.wtbot.components.WorkerFactory

class Main {
    companion object {
        private val log = LogManager.getLogger()

        @JvmStatic
        fun main(args: Array<String>) {
            val worker = WorkerFactory.create().worker()

            Runtime.getRuntime().addShutdownHook(Thread {
                log.info("Interrupt Signal Received")
                worker.jda.shutdown()
            })
        }
    }
}
