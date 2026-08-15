package com.sharazan.core.source.yaml

import com.charleskorn.kaml.Yaml
import com.sharazan.core.source.ConfigurationLoader
import com.sharazan.core.source.ConfigurationSource
import java.io.File

class YamlConfigurationLoader : ConfigurationLoader {

    override fun supports(path: String) = path.endsWith(".yaml") || path.endsWith(".yml")

    override fun load(path: String): ConfigurationSource {
        val node = Yaml.default.parseToYamlNode(File(path).readText())

        return YamlConfigurationSource(node)
    }

}
