package team.false_.wtbot.exceptions

import net.dv8tion.jda.api.entities.MessageChannel

class EmptyMentionedRolesException(
    channel: MessageChannel
) : CallbackException(
    channel,
    "Mentioned Roles are required"
)