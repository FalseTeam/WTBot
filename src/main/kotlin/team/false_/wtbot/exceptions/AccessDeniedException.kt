package team.false_.wtbot.exceptions

import team.false_.wtbot.entities.AccessLevel

class AccessDeniedException private constructor(message: String) : WTBotException("Access Denied", message) {
    companion object {
        fun require(requiredAccessLevel: AccessLevel, currentAccessLevel: AccessLevel) =
            AccessDeniedException("Required Access Level is $requiredAccessLevel\nYour Access Level is $currentAccessLevel")
    }
}