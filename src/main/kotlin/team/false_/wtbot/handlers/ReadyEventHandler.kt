package team.false_.wtbot.handlers

import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.ReadyEvent
import org.apache.logging.log4j.LogManager
import reactor.core.Disposable
import javax.inject.Inject

class ReadyEventHandler @Inject constructor() : Handler() {
    companion object {
        private val log = LogManager.getLogger()
    }

    override fun subscribe(): Disposable {
        return manager.on<ReadyEvent>()
            .subscribe { log.info("Logged in as ${it.jda.selfUser.asTag}") }
    }
}