package com.sharazan.core

import org.http4k.core.Request
import org.http4k.lens.RequestKey


inline fun <reified T: Any> Request.getContext(name: String): T {
    val keyLens = RequestKey.required<T>(name)

    return keyLens(this)
}


inline fun <reified T: Any> Request.withContext(name: String, value: T): Request {
    val keyLens = RequestKey.required<T>(name)

    return keyLens(value,this)
}

