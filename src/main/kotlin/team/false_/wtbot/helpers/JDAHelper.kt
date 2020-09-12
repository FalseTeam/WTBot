package team.false_.wtbot.helpers

import net.dv8tion.jda.api.JDA
import team.false_.wtbot.Config

val JDA.guild get() = this.getGuildById(Config.GUILD_ID)!!
val JDA.staffLogs get() = this.guild.getTextChannelById(Config.CHANNEL_STAFF_LOG_ID)!!
val JDA.voiceLogs get() = this.guild.getTextChannelById(Config.CHANNEL_VOICE_LOG_ID)!!
val JDA.textLogs get() = this.guild.getTextChannelById(Config.CHANNEL_TEXT_LOG_ID)!!