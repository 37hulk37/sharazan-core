package com.sharazan.core.support

import kotlinx.serialization.Serializable

@Serializable
data class TestHttpProperties(
    val host: String = "127.0.0.1",
    val port: Int = 9090
)