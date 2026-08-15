package com.sharazan.core.configuration

import com.sharazan.core.AppBuilder
import com.sharazan.core.source.ConfigurationSource
import com.sharazan.core.source.properties.PropertiesConfigurationLoader
import com.sharazan.core.source.yaml.YamlConfigurationLoader
import org.koin.dsl.module
import java.io.File


fun AppBuilder.properties(path: String) = apply {
    val propertiesModule = module {
        single<ConfigurationSource> { configurationSource(path) }
    }

    addModule(propertiesModule)
}

private fun configurationSource(path: String): ConfigurationSource {
    val file = File(path)
    val loader = when(file.extension) {
        "yaml" -> YamlConfigurationLoader()
        "properties" -> PropertiesConfigurationLoader()
        else -> PropertiesConfigurationLoader()
    }

    return loader.load(path)
}