package team.false_.wtbot.exceptions

class EmptyMentionsException(message: String?) : WTBotException(message) {
    companion object {
        val default = EmptyMentionsException("Mentioned Members and Roles are required")
    }
}