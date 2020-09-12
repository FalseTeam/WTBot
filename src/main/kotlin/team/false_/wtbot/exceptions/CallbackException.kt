package team.false_.wtbot.exceptions

import net.dv8tion.jda.api.entities.MessageChannel

open class CallbackException(val channel: MessageChannel, message: String?) : RuntimeException(message)