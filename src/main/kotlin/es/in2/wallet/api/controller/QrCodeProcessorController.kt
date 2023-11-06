package es.in2.wallet.api.controller

import es.in2.wallet.api.exception.NoAuthorizationFoundException
import es.in2.wallet.api.model.dto.QrContentDTO
import es.in2.wallet.api.service.QrCodeProcessorService
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "QR Codes", description = "QR code management API")
@RestController
@RequestMapping("/api/execute-content")
class QrCodeProcessorController(
    private val qrCodeProcessorService: QrCodeProcessorService
) {

    private val log: Logger = LogManager.getLogger(QrCodeProcessorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun executeQrContent(@RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String, @RequestBody qrContentDTO: QrContentDTO): Any {
        if (authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            val errorMessage = "No Bearer token found in Authorization header"
            throw NoAuthorizationFoundException(errorMessage)
        }
        val token = authorizationHeader.substring(7)
        log.info("QrCodeProcessorController.executeQrContent()")
        return qrCodeProcessorService.processQrContent(qrContentDTO.content, token)
    }

}
