package team.false_.wtbot.exceptions

import net.dv8tion.jda.api.entities.MessageChannel

class EmptyMentionsException(
    channel: MessageChannel
) : CallbackException(
    channel,
    "Mentioned Members and Roles are required"
)