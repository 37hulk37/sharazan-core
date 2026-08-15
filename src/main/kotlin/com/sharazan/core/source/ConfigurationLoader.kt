package com.sharazan.core.source

interface ConfigurationLoader {

    fun supports(path: String): Boolean

    fun load(path: String): ConfigurationSource

}
