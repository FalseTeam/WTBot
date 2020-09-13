package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.Message

val Message.mentionedRolesAllowed
    get() = this.mentionedRoles.filter { this.member!!.accessLevel >= it.requiredAccessLevel }
val Message.mentionedRolesDenied
    get() = this.mentionedRoles.filter { this.member!!.accessLevel < it.requiredAccessLevel }