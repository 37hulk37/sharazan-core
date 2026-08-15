package com.sharazan.core.properties

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties as PropertiesFormat
import kotlinx.serialization.properties.decodeFromStringMap
import java.util.Properties

class ConfigurationSource(
    val properties: Properties
) {

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> get(prefix: String): T {
        val scopedPrefix = "$prefix."

        val scoped = properties.stringPropertyNames()
            .filter { it.startsWith(scopedPrefix) }
            .associate { it.removePrefix(scopedPrefix) to properties.getProperty(it) }

        return PropertiesFormat.decodeFromStringMap(scoped)
    }

}