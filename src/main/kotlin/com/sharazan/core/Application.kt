package com.sharazan.core

import org.koin.core.component.KoinComponent
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.reflect.KClass

class Application(
    extensions: Map<KClass<*>, Any?>,
): KoinComponent, Lifecycle {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    private val started = AtomicBoolean(false)

    private val lifecycles = extensions.values.filterIsInstance<Lifecycle>()

    override fun started() {
        lifecycles.forEach { it.started() }

        shutdownHook()

        started.compareAndSet(false,true)

        logger.trace("Application started")
    }

    fun stop() {
        lifecycles.forEach { it.close() }

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