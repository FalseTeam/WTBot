package team.false_.wtbot.helpers

import net.dv8tion.jda.api.entities.MessageChannel
import team.false_.wtbot.Config

val MessageChannel.isCommandChannel get() = this.idLong == Config.CHANNEL_BOT_ID