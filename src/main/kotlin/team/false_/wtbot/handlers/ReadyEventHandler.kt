package team.false_.wtbot.handlers

import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.ReadyEvent
import reactor.core.Disposable
import team.false_.wtbot.log

class ReadyEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.on<ReadyEvent>()
            .subscribe {
                log.info("Logged in as ${it.jda.selfUser.asTag}")
            }
    }
}