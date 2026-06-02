package com.sharazan.core

import kotlin.reflect.KClass

class AppBuilder {

    private val extensions = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> install(config: T) {
        extensions[config::class] = config
    }

    fun <T : Any> get(type: KClass<T>): T? {
        return extensions[type] as T?
    }

    inline fun <reified T : Any> get(): T? =
        get(T::class)

    fun build(): Application {
        return Application(extensions.toMap())
    }

}