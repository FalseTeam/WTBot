package team.false_.wtbot.helpers

import club.minnced.jda.reactor.asMono
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import team.false_.wtbot.config.Config

val Member.accessLevel get() = this.roles.map { it.idLong }.map(Config::accessLevel).toSet().maxOf { it }

fun Member.addRole(role: Role) = this.guild.addRoleToMember(this, role).asMono()
fun Member.removeRole(role: Role) = this.guild.removeRoleFromMember(this, role).asMono()