package io.xdatagroup.test.task.dto

class ErrorMessage(
    val message: String,
    val innerError: InnerError? = null
)

class InnerError(

    val message: String? = null,
    val cause: String? = null,
    val httpReason: String? = null,
    val source: String? = null
)
