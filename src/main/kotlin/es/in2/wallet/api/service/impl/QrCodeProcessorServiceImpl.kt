package es.in2.wallet.api.service.impl

import es.in2.wallet.api.exception.NoSuchQrContentException
import es.in2.wallet.api.model.entity.QrType
import es.in2.wallet.api.service.QrCodeProcessorService
import es.in2.wallet.api.utils.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class QrCodeProcessorServiceImpl(

        @Value("\${app.url.wca-baseurl}") private val wcaBaseUrl: String,
        private val applicationUtils: ApplicationUtils

) : QrCodeProcessorService {

    private val log: Logger = LogManager.getLogger(QrCodeProcessorServiceImpl::class.java)

    override fun processQrContent(qrContent: String, token: String): Any {

        log.debug("Processing QR content: $qrContent")

        return when (identifyQrContentType(qrContent)) {
            QrType.SIOP_AUTH_REQUEST_URI -> {
                log.info("Processing SIOP authentication request URI")
                val url = wcaBaseUrl + GET_SIOP_AUTHENTICATION_URI

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    HEADER_AUTHORIZATION to "Bearer $token"
                )

                applicationUtils.postRequest(url=url, headers = headers, body = "{\"qr_content\":\"$qrContent\"}")
                /*
                val redirectUrl = wcaBaseUrl + GET_SIOP_AUTHENTICATION_URI
                val responseBody = "{\"qr_content\":\"$qrContent\"}"

                val headers = HttpHeaders()
                headers.add("Location", redirectUrl)

                return ResponseEntity(responseBody, headers, HttpStatus.FOUND)

                 */
            }

            QrType.SIOP_AUTH_REQUEST -> {
                log.info("Processing SIOP authentication request")
                val url = wcaBaseUrl + PROCESS_SIOP_AUTHENTICATION_REQUEST

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    HEADER_AUTHORIZATION to "Bearer $token"
                )
                applicationUtils.postRequest(url=url, headers = headers, body = "{\"qr_content\":\"$qrContent\"}")
            }

            QrType.CREDENTIAL_OFFER_URI -> {
                log.info("Processing verifiable credential offer URI")
                val url = wcaBaseUrl + GET_CREDENTIAL_ISSUER_METADATA

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    HEADER_AUTHORIZATION to "Bearer $token"
                )
                applicationUtils.postRequest(url=url, headers = headers, body = "{\"qr_content\":\"$qrContent\"}")
            }

            QrType.VC_JWT -> {
                log.info("Saving verifiable credential in VC JWT format")
                //orionService.saveVC(qrContent)
            }

            QrType.UNKNOWN -> {
                val errorMessage = "The received QR content cannot be processed"
                log.warn(errorMessage)
                throw NoSuchQrContentException(errorMessage)
            }
        }
    }

    private fun identifyQrContentType(content: String): QrType {

        // define multiple regex patterns to identify the QR content type

        val loginRequestUrlRegex = Regex("(https|http).*?(authentication-request|authentication-requests).*")
        val siopAuthenticationRequestRegex = Regex("openid://.*")
        val credentialOfferUriRegex = Regex("(https|http).*?(credential-offer).*")
        val verifiableCredentialInVcJwtFormatRegex = Regex("ey.*")

        return when {
            loginRequestUrlRegex.matches(content) -> QrType.SIOP_AUTH_REQUEST_URI
            siopAuthenticationRequestRegex.matches(content) -> QrType.SIOP_AUTH_REQUEST
            credentialOfferUriRegex.matches(content) -> QrType.CREDENTIAL_OFFER_URI
            verifiableCredentialInVcJwtFormatRegex.matches(content) -> QrType.VC_JWT
            else -> {
                log.warn("Unknown QR content type: $content")
                QrType.UNKNOWN
            }
        }
    }


}