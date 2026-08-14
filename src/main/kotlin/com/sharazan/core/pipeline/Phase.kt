package com.sharazan.core.pipeline

import org.http4k.core.Request
import org.http4k.core.Response
import org.slf4j.LoggerFactory

class Phase(
    private val name: String,
    private val interceptors: List<Interceptor>,
) {

    private val logger = LoggerFactory.getLogger(Phase::class.java)

    fun preProcess(request: Request): Request {
        logger.trace("Started phase $name pre-processing request: {}", request)

        return interceptors.fold(request) { current, interceptor ->
            interceptor.before(current)
        }
    }

    fun postProcess(request: Request, response: Response): Response {
        logger.trace("Started phase $name post-processing response: {}", response)

        return interceptors.asReversed().fold(response) { current, interceptor ->
            interceptor.after(request, current)
        }
    }

}