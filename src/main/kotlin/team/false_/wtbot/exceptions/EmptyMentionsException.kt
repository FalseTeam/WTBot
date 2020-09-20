package team.false_.wtbot.exceptions

class EmptyMentionsException private constructor(message: String) : IllegalArgumentException(message) {
    companion object {
        val membersRoles = EmptyMentionsException("Mentioned Members and Roles are required")
        val members = EmptyMentionsException("Mentioned Members are required")
        val channels = EmptyMentionsException("Mentioned Channels are required")
    }
}