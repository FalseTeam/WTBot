package team.false_.wtbot.helpers

import net.dv8tion.jda.api.JDA
import team.false_.wtbot.Config

val JDA.guild get() = this.getGuildById(Config.GUILD_ID)!!
val JDA.staffLogs get() = this.guild.getTextChannelById(Config.CHANNEL_LOG_STAFF_ID)!!
val JDA.voiceLogs get() = this.guild.getTextChannelById(Config.CHANNEL_LOG_VOICE_ID)!!
val JDA.textLogs get() = this.guild.getTextChannelById(Config.CHANNEL_LOG_TEXT_ID)!!
val JDA.staffChannel get() = this.guild.getTextChannelById(Config.CHANNEL_STAFF_ID)!!
val JDA.exceptionsChannel get() = this.guild.getTextChannelById(Config.CHANNEL_LOG_EXCEPTION_ID)!!