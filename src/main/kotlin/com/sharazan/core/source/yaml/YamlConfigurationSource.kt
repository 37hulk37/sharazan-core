package com.sharazan.core.source.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.sharazan.core.source.ConfigurationSource
import kotlinx.serialization.DeserializationStrategy

class YamlConfigurationSource(
    private val root: YamlNode
) : ConfigurationSource {

    override fun <T : Any> get(prefix: String, deserializer: DeserializationStrategy<T>): T {
        val scoped = prefix.split(".").fold(root) { node, key ->
            (node as YamlMap).get<YamlNode>(key) ?: error("Missing YAML key: $prefix")
        }

        return Yaml.default.decodeFromYamlNode(deserializer, scoped)
    }

}
