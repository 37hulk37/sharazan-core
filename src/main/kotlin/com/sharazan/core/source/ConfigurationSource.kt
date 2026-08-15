package com.sharazan.core.source

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer

interface ConfigurationSource {

    fun <T : Any> get(prefix: String, deserializer: DeserializationStrategy<T>): T

}

inline fun <reified T : Any> ConfigurationSource.get(prefix: String): T =
    get(prefix, serializer())
