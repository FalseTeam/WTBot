package team.false_.wtbot.handlers.commands.roles

import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Roles
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.handlers.commands.abstract.StaffCommand

class UnBanChatCommand : StaffCommand() {
    override fun exec(it: Message): Flux<out Any> {
        return if (it.mentionedMembers.isEmpty())
            Flux.error(EmptyMentionsException.default)
        else
            RoleRemoveCommand.exec(
                it.author,
                it.channel,
                it.mentionedMembers,
                listOf(it.jda.getRoleById(Roles.CHAT_BANNED)!!),
                listOf()
            )
    }
}