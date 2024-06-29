package ru.ushakov.otushub.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private const val SERVER_ERROR_MESSAGE = "Ошибка сервера"

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error(SERVER_ERROR_MESSAGE, e)
        return ResponseEntity(
            ErrorResponse(listOf(e.message ?: SERVER_ERROR_MESSAGE)),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}