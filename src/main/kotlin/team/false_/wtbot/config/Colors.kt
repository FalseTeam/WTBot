package team.false_.wtbot.config

object Colors {
    /*
    Colors
     */

    private const val RED = 0xD64045
    private const val PINK = 0xEF476F
    private const val ORANGE = 0xF37748
    private const val YELLOW = 0xFFD166
    private const val GREEN = 0x9FD356
    private const val AQUAMARINE = 0x06D6A0
    private const val CYAN = 0x25CED1
    private const val BLUE = 0x118AB2
    private const val SHADOW_BLUE = 0x416788

    /*
    Elements
     */

    // Response
    const val SUCCESS = GREEN
    const val WARN = YELLOW
    const val ERROR = RED

    // Logging
    const val ROLE_ADD = CYAN
    const val ROLE_REMOVE = ORANGE
    const val VOICE_JOIN = AQUAMARINE
    const val VOICE_MOVE = YELLOW
    const val VOICE_LEAVE = SHADOW_BLUE
    const val VOICE_GUILD_MUTE = PINK
    const val VOICE_GUILD_UNMUTE = BLUE
}