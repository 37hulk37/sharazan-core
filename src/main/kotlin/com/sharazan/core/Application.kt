package com.sharazan.core

import org.koin.core.KoinApplication
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class Application(
    private val koinApplication: KoinApplication,
): Lifecycle, Closeable {

    private val logger = LoggerFactory.getLogger(Application::class.java)

    private val started = AtomicBoolean(false)

    private val lifecycles = koinApplication.koin.getAll<Lifecycle>()


    override fun onStart() {
        lifecycles.forEach { it.onStart() }

        shutdownHook()

        started.compareAndSet(false,true)

        logger.info("Application started")
    }

    override fun onStop() {
        lifecycles.forEach { it.onStop() }
        koinApplication.close()


        started.compareAndSet(true,false)

        logger.info("Application stopped")
    }

    override fun close() {
        onStop()
    }

    private fun shutdownHook() {
        Runtime.getRuntime()
            .addShutdownHook(thread(start = false, name = "shutdown-hook") {
                this.close()
            })
    }

}