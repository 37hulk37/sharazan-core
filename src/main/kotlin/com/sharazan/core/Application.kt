package com.sharazan.core

import org.koin.core.component.KoinComponent
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.reflect.KClass

class Application(
    extensions: Map<KClass<*>, Any?>,
): KoinComponent, Startable, Closeable {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    private val started = AtomicBoolean(false)

    private val startable = extensions.values.filterIsInstance<Startable>()
    private val closeable = extensions.values.filterIsInstance<Closeable>()

    override fun started() {
        startable.forEach { it.started() }

        shutdownHook()

        started.compareAndSet(false,true)

        logger.trace("Application started")
    }

    fun stop() {
        closeable.forEach { it.close() }

        started.compareAndSet(true,false)

        logger.trace("Application stopped")
    }

    override fun close() = stop()

    private fun shutdownHook() {
        Runtime.getRuntime()
            .addShutdownHook(thread(start = false, name = "shutdown-hook") {
                this.close()
            })
    }

}