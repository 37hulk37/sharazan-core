package com.sharazan.core.configuration

import com.sharazan.core.AppBuilder
import com.sharazan.core.properties.ConfigurationSource
import org.koin.core.Koin
import java.io.FileInputStream
import java.util.Properties

fun AppBuilder.koin(koin: Koin) = apply {
    install(koin)
}

fun AppBuilder.properties(source: ConfigurationSource) = apply {
    install(source)
}

fun loadProperties(path: String): ConfigurationSource {
    val properties = Properties()

    FileInputStream(path)
        .use { properties.load(it) }

    return ConfigurationSource(properties)
}