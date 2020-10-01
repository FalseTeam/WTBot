package team.false_.wtbot.utils

import club.minnced.jda.reactor.ReactiveEventManager
import team.false_.wtbot.handlers.Handler
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

infix fun KClass<*>.doesOverride(method: KFunction<*>): Boolean {
    return this.memberFunctions.first { it.name == method.name } in this.declaredFunctions
}

@Suppress("UNCHECKED_CAST")
fun KClass<out Handler>.createInstance(manager: ReactiveEventManager): Handler {
    return (primaryConstructor as (ReactiveEventManager) -> Handler)(manager)
}
