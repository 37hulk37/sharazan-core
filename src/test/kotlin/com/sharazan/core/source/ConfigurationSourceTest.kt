package com.sharazan.core.source

import com.sharazan.core.source.properties.PropertiesConfigurationLoader
import com.sharazan.core.source.yaml.YamlConfigurationLoader
import com.sharazan.core.support.TestHttpProperties
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationSourceTest {

    private val properties = TestHttpProperties()


    @Test
    fun `'yaml' file is loaded and decoded correctly`() {
        val source = YamlConfigurationLoader()
            .load(resourcePath("test-application.yaml"))

        assertEquals(TestHttpProperties(), source.get<TestHttpProperties>("sharazan.http"))
    }

    @Test
    fun `'yml' file is loaded and decoded correctly`() {
        val source = YamlConfigurationLoader()
            .load(resourcePath("test-application.yml"))

        assertEquals(TestHttpProperties(), source.get<TestHttpProperties>("sharazan.http"))
    }

    @Test
    fun `'properties' file is loaded and decoded correctly`() {
        val source = PropertiesConfigurationLoader()
            .load(resourcePath("test-application.properties"))

        assertEquals(TestHttpProperties(), source.get<TestHttpProperties>("sharazan.http"))
    }

    private fun resourcePath(name: String): String {
        val resource = requireNotNull(javaClass.classLoader.getResource(name)) {
            "$name not found on classpath"
        }

        return File(resource.toURI()).path
    }

}
