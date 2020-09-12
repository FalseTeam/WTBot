package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.Member
import team.false_.wtbot.Config
import team.false_.wtbot.entities.AccessLevel


val Member.accessLevel
    get() = this.roles
        .map { it.idLong }
        .map { Config.ACCESS_LEVELS.getOrDefault(it, AccessLevel.DEFAULT) }
        .toSet()
        .maxOf { it }
