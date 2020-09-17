package team.false_.wtbot.exceptions

class EmptyMentionsException private constructor(message: String) : WTBotException("Illegal Argument", message) {
    companion object {
        val membersRoles = EmptyMentionsException("Mentioned Members and Roles are required")
        val members = EmptyMentionsException("Mentioned Members are required")
    }
}