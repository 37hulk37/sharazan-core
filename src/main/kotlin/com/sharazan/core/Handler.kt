package com.sharazan.core

import org.http4k.core.Request
import org.http4k.core.Response

interface Handler {

    suspend fun handle(request: Request): Response

}