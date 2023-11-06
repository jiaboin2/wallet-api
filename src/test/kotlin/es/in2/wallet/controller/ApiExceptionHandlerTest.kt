package es.in2.wallet.controller

import es.in2.wallet.api.exception.NoSuchQrContentException
import es.in2.wallet.api.exception.handler.ApiExceptionHandler
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig


@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class ApiExceptionHandlerTest {

    @Test
    fun testHandleNoSuchQrContentException() {
        val exception = NoSuchQrContentException("Unknown QR content")
        val response = ApiExceptionHandler().handleNoSuchQrContentException(exception)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun testHandleNoAuthorizationFoundException() {
        val exception = NoSuchQrContentException("No Authorization")
        val response = ApiExceptionHandler().handleNoAuthorizationFoundException(exception)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

}