package team.false_.wtbot.helpers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import team.false_.wtbot.Config
import team.false_.wtbot.entities.AccessLevel

val Member.accessLevel
    get() = this.roles.map { it.idLong }
        .map { Config.ACCESS_LEVELS.getOrDefault(it, AccessLevel.DEFAULT) }
        .toSet().maxOf { it }

fun Member.addRole(role: Role) = this.guild.addRoleToMember(this, role).asMono()
fun Member.delRole(role: Role) = this.guild.removeRoleFromMember(this, role).asMono()