package team.false_.wtbot.exceptions

open class IllegalArgumentException(message: String) : WTBotException("Illegal Argument", message) {
    companion object {
        val noAllowedChannelsFound = IllegalArgumentException("No allowed channels found")
    }
}