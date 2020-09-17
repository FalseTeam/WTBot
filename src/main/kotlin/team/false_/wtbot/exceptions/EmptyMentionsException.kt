package team.false_.wtbot.exceptions

class EmptyMentionsException(message: String?) : WTBotException(message) {
    companion object {
        val membersRoles = EmptyMentionsException("Mentioned Members and Roles are required")
        val members = EmptyMentionsException("Mentioned Members are required")
    }
}