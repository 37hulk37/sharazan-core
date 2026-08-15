package com.sharazan.core.exception

import org.http4k.core.Status
import org.slf4j.LoggerFactory
import java.time.Instant

private val logger = LoggerFactory.getLogger("com.sharazan.http.core.Errors")


fun handleException(message: String, t: Throwable? = null): ErrorInfo {
    var status: Status = Status.BAD_REQUEST
    if (t is ApplicationException) {
        status = t.status
    }

    logger.error("handle request with exception $t")

    return ErrorInfo(
        message,
        status.code,
        Instant.now().toString()
    )
}