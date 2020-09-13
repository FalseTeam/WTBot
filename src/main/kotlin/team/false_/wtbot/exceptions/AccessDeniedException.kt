package team.false_.wtbot.exceptions

import team.false_.wtbot.entities.AccessLevel

class AccessDeniedException(message: String?) : WTBotException(message) {
    companion object {
        val default = AccessDeniedException("Access Denied")
        fun require(requiredAccessLevel: AccessLevel, currentAccessLevel: AccessLevel) =
            AccessDeniedException("Access Denied. Require Access Level is $requiredAccessLevel. Your Access Level is $currentAccessLevel")
    }
}