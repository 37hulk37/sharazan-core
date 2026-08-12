package com.sharazan.core.pipeline

import org.http4k.core.Request
import org.http4k.core.Response
import org.slf4j.LoggerFactory

class Pipeline(
    private val phases: List<Phase>,
) {

    private val logger = LoggerFactory.getLogger(Pipeline::class.java)


    fun preProcess(request: Request): Request {
        if (phases.isEmpty()) {
            return request
        }

        logger.trace("Pre-processing request: {}", request)

        return phases.fold(request) { current, phase ->
            phase.preProcess(current)
        }
    }

    fun postProcess(response: Response): Response {
        if (phases.isEmpty()) {
            return response
        }

        logger.trace("Post-processing response {}", response)

        return phases.fold(response) { current, phase ->
            phase.postProcess(current)
        }
    }

}