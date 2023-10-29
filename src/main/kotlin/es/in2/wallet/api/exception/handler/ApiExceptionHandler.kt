package es.in2.wallet.api.exception.handler

import es.in2.wallet.api.exception.NoAuthorizationFoundException
import es.in2.wallet.api.exception.NoSuchQrContentException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    private val log: Logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(NoSuchQrContentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoSuchQrContentException(e: Exception): ResponseEntity<Unit> {
        log.error(e.message)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoAuthorizationFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoAuthorizationFoundException(e: Exception): ResponseEntity<Unit> {
        log.error(e.message)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

}