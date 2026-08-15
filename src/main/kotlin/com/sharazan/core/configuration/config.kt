package com.sharazan.core.configuration

import com.sharazan.core.AppBuilder
import com.sharazan.core.properties.ConfigurationSource
import org.koin.dsl.module
import java.io.FileInputStream
import java.util.Properties

fun AppBuilder.properties(path: String) = apply {
    val propertiesModule = module {
        single { loadProperties(path) }
    }

    addModule(propertiesModule)
}

private fun loadProperties(path: String): ConfigurationSource {
    val properties = Properties()

    FileInputStream(path)
        .use { properties.load(it) }

    return ConfigurationSource(properties)
}