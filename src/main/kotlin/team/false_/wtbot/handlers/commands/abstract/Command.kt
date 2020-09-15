package team.false_.wtbot.handlers.commands.abstract

import net.dv8tion.jda.api.entities.Message
import org.reactivestreams.Publisher
import team.false_.wtbot.entities.AccessLevel

abstract class Command {
    abstract val requireAccessLevel: AccessLevel
    abstract fun exec(it: Message): Publisher<out Any>
}