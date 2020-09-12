package team.false_.wtbot

import team.false_.wtbot.entities.AccessLevel

class Config {
    companion object {
        val GUILD_ID: Long = 381730844588638208
        val CHANNEL_BOT_ID: Long = 753630798896431114
        val CHANNEL_STAFF_LOG_ID: Long = 743042878015799387
        val CHANNEL_VOICE_LOG_ID: Long = 743042808570445844
        val CHANNEL_TEXT_LOG_ID: Long = 743042836446052353
        val BYPASS_CHANNELS = setOf(616984490719313930, 737489022628135002, 736948549694128211)
        val ACCESS_LEVELS = mapOf(
            Pair(381733450136289281, AccessLevel.ADMINISTRATOR),
            Pair(596298760091467776, AccessLevel.STAFF),
            Pair(733314793926033410, AccessLevel.JUNIOR)
        )
        val REQUIRED_ACCESS_LEVELS = mapOf(
            Pair(596298760091467776, AccessLevel.ADMINISTRATOR), // STAFF
            Pair(733314793926033410, AccessLevel.ADMINISTRATOR), // JUNIOR
            Pair(596298479953903617, AccessLevel.ADMINISTRATOR), // IT
            Pair(736571192076533891, AccessLevel.ADMINISTRATOR), // BETA TESTER
            Pair(730137037231095869, AccessLevel.ADMINISTRATOR), // GOLD
            Pair(733376779124736071, AccessLevel.ADMINISTRATOR), // K620
            Pair(722401190520029245, AccessLevel.STAFF), // MEDIA
            Pair(706206229143224321, AccessLevel.STAFF), // ACTIVE
            Pair(735605991592427582, AccessLevel.JUNIOR), // CHAT BANNED
            Pair(737265315804872704, AccessLevel.JUNIOR), // VOICE BANNED
        )
    }
}