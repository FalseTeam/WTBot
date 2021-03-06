package team.false_.wtbot.config

import team.false_.wtbot.entities.AccessLevel

object Config {
    const val GUILD = 381730844588638208

    fun accessLevel(roleId: Long) = when (roleId) {
        Roles.ADMINISTRATOR -> AccessLevel.ADMINISTRATOR
        Roles.STAFF -> AccessLevel.STAFF
        Roles.JUNIOR -> AccessLevel.JUNIOR
        else -> AccessLevel.DEFAULT
    }

    fun requiredAccessLevel(roleId: Long) = when (roleId) {
        Roles.STAFF -> AccessLevel.ADMINISTRATOR
        Roles.JUNIOR -> AccessLevel.ADMINISTRATOR
        Roles.IT -> AccessLevel.ADMINISTRATOR
        Roles.BETA_TESTER -> AccessLevel.ADMINISTRATOR
        Roles.GOLD -> AccessLevel.ADMINISTRATOR
        Roles.K620 -> AccessLevel.ADMINISTRATOR
        Roles.MEDIA -> AccessLevel.STAFF
        Roles.ACTIVE -> AccessLevel.STAFF
        Roles.GULAG -> AccessLevel.STAFF
        Roles.CHAT_BANNED -> AccessLevel.JUNIOR
        Roles.VOICE_BANNED -> AccessLevel.JUNIOR
        else -> AccessLevel.UNREACHABLE
    }
}