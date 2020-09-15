package team.false_.wtbot.handlers.commands.abstract

import team.false_.wtbot.entities.AccessLevel

abstract class StaffCommand : Command() {
    override val requireAccessLevel = AccessLevel.JUNIOR
}