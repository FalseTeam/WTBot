package team.false_.wtbot.handlers

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import reactor.core.Disposable
import reactor.core.publisher.Flux
import team.false_.wtbot.config.Channels
import team.false_.wtbot.exceptions.AccessDeniedException
import team.false_.wtbot.handlers.commands.AccessLevelCommand
import team.false_.wtbot.handlers.commands.ColorCommand
import team.false_.wtbot.handlers.commands.ColorRandomCommand
import team.false_.wtbot.handlers.commands.roles.*
import team.false_.wtbot.utils.accessLevel
import team.false_.wtbot.utils.subscribeOnAnyWithHandleError

class MessageReceivedEventHandler : Handler() {
    private val commands = mapOf(
        Channels.BOT to mapOf(
            "addrole" to RoleAddCommand(),
            "cb" to BanChatCommand(),
            "vb" to BanVoiceCommand(),
            "ban" to BanChatVoiceCommand(),
            "delrole" to RoleRemoveCommand(),
            "uncb" to UnBanChatCommand(),
            "unvb" to UnBanVoiceCommand(),
            "unban" to UnBanChatVoiceCommand(),
        ),
        Channels.TEST_CHANNEL to mapOf(
            "color" to ColorCommand(),
            "random" to ColorRandomCommand(),
            "accesslevel" to AccessLevelCommand(),
        ),
    )

    override fun subscribe(): Disposable {
        return manager.subscribeOnAnyWithHandleError<MessageReceivedEvent> { event ->
            Flux.just(event).map { it.message }
                .filter {
                    commands.containsKey(it.channel.idLong) && it.contentRaw.isNotEmpty() &&
                            with(it.contentRaw[0]) { this == '.' || this == '!' }
                }
                .map { it to commands[it.channel.idLong]!![it.contentRaw.split(' ')[0].substring(1)] }
                .filter { it.second != null }
                .flatMap {
                    if (it.first.member!!.accessLevel >= it.second!!.requireAccessLevel)
                        it.second!!.exec(it.first)
                    else
                        Flux.error(
                            AccessDeniedException.require(
                                it.second!!.requireAccessLevel,
                                it.first.member!!.accessLevel
                            )
                        )
                }
        }
    }
}