package team.false_.wtbot.exceptions

open class WTBotException protected constructor(val title: String, message: String) : RuntimeException(message)