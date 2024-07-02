package ru.ushakov.otushub.exception

import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.naming.ServiceUnavailableException

private const val SERVER_ERROR_MESSAGE = "Ошибка сервера"

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Value("\${retry.after.seconds}")
    private lateinit var retryAfterSeconds: String

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error(SERVER_ERROR_MESSAGE, e)
        val headers = HttpHeaders()
        headers.add("Retry-After", retryAfterSeconds)
        return ResponseEntity(
            ErrorResponse(listOf(SERVER_ERROR_MESSAGE)),
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(ServiceUnavailableException::class)
    fun handleServiceUnavailableException(e: ServiceUnavailableException): ResponseEntity<ErrorResponse> {
        val headers = HttpHeaders()
        headers.add("Retry-After", retryAfterSeconds)
        return ResponseEntity(
            ErrorResponse(listOf(SERVER_ERROR_MESSAGE)),
            headers,
            HttpStatus.SERVICE_UNAVAILABLE
        )
    }

    @ExceptionHandler(ValueInstantiationException::class)
    fun handleValueInstantiationException(e: ServiceUnavailableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(listOf(e.message.toString())),
            HttpStatus.BAD_REQUEST
        )
    }
}