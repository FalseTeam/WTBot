package team.false_.wtbot.handlers.commands.roles

import net.dv8tion.jda.api.entities.Message
import reactor.core.publisher.Flux
import team.false_.wtbot.exceptions.EmptyMentionsException
import team.false_.wtbot.handlers.commands.abstract.StaffCommand
import team.false_.wtbot.utils.mentionedRolesAllowed
import team.false_.wtbot.utils.mentionedRolesDenied
import team.false_.wtbot.utils.removeRole

class RoleRemoveCommand : StaffCommand() {
    override fun exec(it: Message): Flux<out Any> {
        return if (it.mentionedMembers.isEmpty() or it.mentionedRoles.isEmpty())
            Flux.error(EmptyMentionsException.membersRoles)
        else
            removeRole(
                it.author,
                it.channel,
                it.mentionedMembers,
                it.mentionedRolesAllowed,
                it.mentionedRolesDenied
            )
    }
}