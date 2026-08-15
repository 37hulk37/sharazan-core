package com.sharazan.core.source.properties

import com.sharazan.core.source.ConfigurationSource
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*
import kotlinx.serialization.properties.Properties as PropertiesFormat

class PropertiesConfigurationSource(
    private val properties: Properties
) : ConfigurationSource {

    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : Any> get(prefix: String, deserializer: DeserializationStrategy<T>): T {
        val scopedPrefix = "$prefix."

        val scoped = properties.stringPropertyNames()
            .filter { it.startsWith(scopedPrefix) }
            .associate { it.removePrefix(scopedPrefix) to properties.getProperty(it) }

        return PropertiesFormat.decodeFromStringMap(deserializer, scoped)
    }

}
