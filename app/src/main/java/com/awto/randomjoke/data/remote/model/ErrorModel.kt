package com.awto.randomjoke.data.remote.model

data class ErrorModel (
    val error: Boolean? = false,
    val internalError: Boolean? = false,
    val code: Long? = -1,
    val message: String? = "",
    val causedBy: List<String>? = emptyList(),
    val additionalInfo: String? = "",
    val timestamp: Long? = -1
)
