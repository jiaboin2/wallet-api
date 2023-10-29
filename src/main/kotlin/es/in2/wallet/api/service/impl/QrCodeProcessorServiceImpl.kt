package es.in2.wallet.api.service.impl

import es.in2.wallet.api.exception.NoAuthorizationFoundException
import es.in2.wallet.api.exception.NoSuchQrContentException
import es.in2.wallet.api.model.entity.QrType
import es.in2.wallet.api.service.QrCodeProcessorService
import es.in2.wallet.api.utils.CONTENT_TYPE
import es.in2.wallet.api.utils.CONTENT_TYPE_APPLICATION_JSON
import es.in2.wallet.api.utils.ApplicationUtils
import es.in2.wallet.api.utils.GET_CREDENTIAL_ISSUER_METADATA
import es.in2.wallet.api.utils.GET_SIOP_AUTHENTICATION_URI
import es.in2.wallet.api.utils.PROCESS_SIOP_AUTHENTICATION_REQUEST
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


@Service
class QrCodeProcessorServiceImpl(

        @Value("\${app.url.verifiable-credential-service-baseurl}") private val verifiableCredentialServiceBaseUrl: String,
        @Value("\${app.url.siop-service-baseurl}") private val siopServiceBaseUrl: String,
        private val applicationUtils: ApplicationUtils

) : QrCodeProcessorService {

    private val log: Logger = LogManager.getLogger(QrCodeProcessorServiceImpl::class.java)

    override fun processQrContent(qrContent: String): Any {

        log.debug("Processing QR content: $qrContent")

        return when (identifyQrContentType(qrContent)) {
            QrType.SIOP_AUTH_REQUEST_URI -> {
                log.info("Processing SIOP authentication request URI")
                val url = siopServiceBaseUrl + GET_SIOP_AUTHENTICATION_URI
                val token = getAuthorizationToken()

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    "Authorization" to "Bearer $token"
                )

                applicationUtils.postRequest(url=url, headers = headers, body = "{\"qr_content\":\"$qrContent\"}")
            }

            QrType.SIOP_AUTH_REQUEST -> {
                log.info("Processing SIOP authentication request")
                val url = siopServiceBaseUrl + PROCESS_SIOP_AUTHENTICATION_REQUEST
                val token = getAuthorizationToken()

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    "Authorization" to "Bearer $token"
                )
                applicationUtils.postRequest(url=url, headers = headers, body = "{\"qr_content\":\"$qrContent\"}")
            }

            QrType.CREDENTIAL_OFFER_URI -> {
                log.info("Processing verifiable credential offer URI")
                val url = verifiableCredentialServiceBaseUrl + GET_CREDENTIAL_ISSUER_METADATA

                val token = getAuthorizationToken()

                val headers = listOf(
                    CONTENT_TYPE to CONTENT_TYPE_APPLICATION_JSON,
                    "Authorization" to "Bearer $token"
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

    private fun getAuthorizationToken(): String {
        // Retrieve the current request
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        // Get the Authorization header
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader.isNullOrEmpty() || !authorizationHeader.startsWith("Bearer ")) {
            val errorMessage = "No Bearer token found in Authorization header"
            log.warn(errorMessage)
            throw NoAuthorizationFoundException(errorMessage)
        }
        return authorizationHeader.substring(7) // 7 is the length of "Bearer "
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