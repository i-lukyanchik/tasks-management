package io.xdatagroup.test.task.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import io.xdatagroup.test.task.dto.ErrorMessage
import io.xdatagroup.test.task.dto.InnerError

private const val DEFAULT_ERROR_MESSAGE = "Internal error"
private const val BAD_REQUEST = "Bad parameters in request exception"

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(e: TaskNotFoundException): ResponseEntity<ErrorMessage> = ResponseEntity.status(HttpStatus.GONE).body(ErrorMessage(e.message))

    @ExceptionHandler(Exception::class)
    fun handleOtherException(
        e: Exception
    ): ResponseEntity<ErrorMessage> {
        logger.error(DEFAULT_ERROR_MESSAGE, e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorMessage(
                    DEFAULT_ERROR_MESSAGE,
                    InnerError(e.message)
                )
            )
    }

    @ExceptionHandler(
        IllegalArgumentException::class,
        MissingServletRequestParameterException::class,
        HttpMessageNotReadableException::class,
        MissingRequestHeaderException::class
    )
    fun handleMissingServletRequestParameterException(e: RuntimeException): ResponseEntity<ErrorMessage> {
        logger.error(BAD_REQUEST, e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage(BAD_REQUEST))
    }

}