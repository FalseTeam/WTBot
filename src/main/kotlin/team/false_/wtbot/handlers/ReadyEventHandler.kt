package team.false_.wtbot.handlers

import club.minnced.jda.reactor.on
import net.dv8tion.jda.api.events.ReadyEvent
import reactor.core.Disposable
import team.false_.wtbot.log
import team.false_.wtbot.utils.guild

class ReadyEventHandler : Handler() {
    override fun subscribe(): Disposable {
        return manager.on<ReadyEvent>()
            .subscribe { event ->
                log.info("Logged in as ${event.jda.selfUser.asTag}")
                event.jda.guild.loadMembers().onSuccess { log.info("Loaded Members") }
            }
    }
}