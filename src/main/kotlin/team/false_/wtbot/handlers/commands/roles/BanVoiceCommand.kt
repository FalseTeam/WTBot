package team.false_.wtbot.handlers.commands.roles

import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Roles
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.handlers.commands.abstract.StaffCommand
import team.false_.wtbot.utils.addRole

class BanVoiceCommand : StaffCommand() {
    override fun exec(it: Message): Flux<out Any> {
        return if (it.mentionedMembers.isEmpty())
            Flux.error(EmptyMentionsException.members)
        else
            addRole(
                it.author,
                it.channel,
                it.mentionedMembers,
                listOf(it.jda.getRoleById(Roles.VOICE_BANNED)!!),
                listOf()
            )
    }
}