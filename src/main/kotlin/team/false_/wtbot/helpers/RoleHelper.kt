package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.Role
import team.false_.wtbot.Config
import team.false_.wtbot.entities.AccessLevel

val Role.requiredAccessLevel
    get() = Config.REQUIRED_ACCESS_LEVELS.getOrDefault(this.idLong, AccessLevel.ADMINISTRATOR)
