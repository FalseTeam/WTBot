package team.false_.wtbot.exceptions

import net.dv8tion.jda.api.entities.MessageChannel

class EmptyMentionedMembersException(
    channel: MessageChannel
) : CallbackException(
    channel,
    "Mentioned Members are required"
)