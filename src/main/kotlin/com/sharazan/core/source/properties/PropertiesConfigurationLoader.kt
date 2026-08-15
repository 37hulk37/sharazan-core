package com.sharazan.core.source.properties

import com.sharazan.core.source.ConfigurationLoader
import com.sharazan.core.source.ConfigurationSource
import java.io.FileInputStream
import java.util.Properties

class PropertiesConfigurationLoader : ConfigurationLoader {

    override fun supports(path: String) = path.endsWith(".properties")

    override fun load(path: String): ConfigurationSource {
        val properties = Properties()

        FileInputStream(path)
            .use { properties.load(it) }

        return PropertiesConfigurationSource(properties)
    }

}
