package com.awto.randomjoke.data.remote.model

data class ErrorModel (
    val error: Boolean,
    val internalError: Boolean,
    val code: Long,
    val message: String,
    val causedBy: List<String>,
    val additionalInfo: String,
    val timestamp: Long
)
