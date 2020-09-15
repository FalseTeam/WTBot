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
    const val WARNING = YELLOW
    const val ERROR = RED

    // Logging
    const val ROLE_ADD = CYAN
    const val ROLE_DEL = ORANGE
    const val VOICE_CHANNEL_CONNECT = AQUAMARINE
    const val VOICE_CHANNEL_CHANGE = YELLOW
    const val VOICE_CHANNEL_DISCONNECT = SHADOW_BLUE
    const val VOICE_SERVER_MUTE = PINK
    const val VOICE_SERVER_UNMUTE = BLUE
}