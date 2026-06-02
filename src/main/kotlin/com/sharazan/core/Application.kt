package com.sharazan.core

import org.koin.core.component.KoinComponent
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.reflect.KClass

class Application(
    extensions: Map<KClass<*>, Any?>,
): KoinComponent, Startable, Closeable {

    private val started = AtomicBoolean(false)

    private val startable = extensions.values.filterIsInstance<Startable>()
    private val closeable = extensions.values.filterIsInstance<Closeable>()

    override fun started() {
        startable.forEach { it.started() }

        shutdownHook()

        started.compareAndSet(false,true)
    }

    fun stop() {
        closeable.forEach { it.close() }

        started.compareAndSet(true,false)
    }

    override fun close() = stop()

    private fun shutdownHook() {
        Runtime.getRuntime()
            .addShutdownHook(thread(start = false, name = "shutdown-hook") {
                this.close()
            })
    }

}