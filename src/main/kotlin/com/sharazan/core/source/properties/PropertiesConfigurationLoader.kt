package com.sharazan.core.source.properties

import com.sharazan.core.source.ConfigurationLoader
import com.sharazan.core.source.ConfigurationSource
import com.sharazan.core.source.resolveEnvPlaceholders
import java.io.File
import java.io.StringReader
import java.util.Properties

class PropertiesConfigurationLoader : ConfigurationLoader {

    override fun supports(path: String) = path.endsWith(".properties")

    override fun load(path: String): ConfigurationSource {
        val properties = Properties()

        StringReader(resolveEnvPlaceholders(File(path).readText()))
            .use { properties.load(it) }

        return PropertiesConfigurationSource(properties)
    }

}
