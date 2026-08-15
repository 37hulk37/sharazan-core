package com.sharazan.core.exception

data class ErrorInfo(
    val message: String,
    val status: Int,
    val createdAt: String,
)