package com.sharazan.core.pipeline

import org.http4k.core.Request
import org.http4k.core.Response

interface Interceptor {

    fun before(request: Request): Request = request

    fun after(response: Response): Response = response

}