package team.false_.wtbot.components

import dagger.Component
import team.false_.wtbot.Worker
import team.false_.wtbot.modules.HandlersModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HandlersModule::class
    ]
)
interface WorkerFactory {
    fun worker(): Worker

    companion object {
        fun create(): WorkerFactory {
            return DaggerWorkerFactory.create()
        }
    }
}