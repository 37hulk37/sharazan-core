package com.sharazan.core.source

import com.sharazan.core.source.properties.PropertiesConfigurationLoader
import com.sharazan.core.source.yaml.YamlConfigurationLoader
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationSourceTest {


    private val properties = mapOf("host" to "127.0.0.1", "port" to "9090")


    @Test
    fun `'yaml' file is loaded and decoded correctly`() {
        val source = YamlConfigurationLoader()
            .load(resourcePath("test-application.yaml"))

        assertEquals(properties, source.get<Map<String, String>>("sharazan.http"))
    }

    @Test
    fun `'yml' file is loaded and decoded correctly`() {
        val source = YamlConfigurationLoader()
            .load(resourcePath("test-application.yml"))

        assertEquals(properties, source.get<Map<String, String>>("sharazan.http"))
    }

    @Test
    fun `'properties' file is loaded and decoded correctly`() {
        val source = PropertiesConfigurationLoader()
            .load(resourcePath("test-application.properties"))

        assertEquals(properties, source.get<Map<String, String>>("sharazan.http"))
    }

    private fun resourcePath(name: String): String {
        val resource = requireNotNull(javaClass.classLoader.getResource(name)) {
            "$name not found on classpath"
        }

        return File(resource.toURI()).path
    }

}
