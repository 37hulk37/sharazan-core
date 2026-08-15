package com.sharazan.core.exception

import org.http4k.core.Status

open class ApplicationException(
    message: String,
    cause: Throwable? = null,
    val status: Status = Status.BAD_REQUEST,
) : RuntimeException(message, cause) {

    constructor(cause: Throwable) :
            this("", cause)

}