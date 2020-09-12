package team.false_.wtbot.modules

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import team.false_.wtbot.handlers.AccessLevelHandler
import team.false_.wtbot.handlers.AddRoleHandler
import team.false_.wtbot.handlers.Handler
import team.false_.wtbot.handlers.ReadyEventHandler

@Module
interface HandlersModule {
    @Binds
    @IntoSet
    fun readyEventHandler(readyEventHandler: ReadyEventHandler): Handler

    @Binds
    @IntoSet
    fun addRoleHandler(addRoleHandler: AddRoleHandler): Handler

    @Binds
    @IntoSet
    fun accessLevelHandler(accessLevelHandler: AccessLevelHandler): Handler
}