package team.false_.wtbot.exceptions

import net.dv8tion.jda.api.entities.MessageChannel
import team.false_.wtbot.entities.AccessLevel

class AccessDeniedException(
    accessLevel: AccessLevel,
    requiredAccessLevel: AccessLevel,
    channel: MessageChannel
) : CallbackException(
    channel,
    "Access Denied. Required Access Level is $requiredAccessLevel. Your Access Level is $accessLevel"
)