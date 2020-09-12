package team.false_.wtbot.modules

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import team.false_.wtbot.handlers.*

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
    fun delRoleHandler(delRoleHandler: DelRoleHandler): Handler

    @Binds
    @IntoSet
    fun accessLevelHandler(accessLevelHandler: AccessLevelHandler): Handler
}