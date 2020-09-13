package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.IMentionable

fun List<IMentionable>.joinDefault() = this.joinToString(", ") { it.asMention }