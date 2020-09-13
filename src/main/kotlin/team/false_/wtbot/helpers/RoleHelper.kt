package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.Role
import team.false_.wtbot.config.Config

val Role.requiredAccessLevel get() = Config.requiredAccessLevel(this.idLong)