package es.in2.wallet.services

import es.in2.wallet.api.exception.NoSuchQrContentException
import es.in2.wallet.api.service.impl.QrCodeProcessorServiceImpl
import es.in2.wallet.api.utils.ApplicationUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest
class QrCodeProcessorServiceImplTest {

    private val applicationUtils: ApplicationUtils = mockk(relaxed = true)

    private lateinit var service: QrCodeProcessorServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = QrCodeProcessorServiceImpl("wcaBaseUrl", applicationUtils)
    }

    @Test
    fun testProcessQrContentCredentialOfferUri() {
        // Mock behavior
        val qrContent = "https://www.goodair.com/credential-offer?credential_offer_uri=https://www.goodair.com/credential-offer/5j349k3e3n23j"
        val token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxOGFyVmZaZTJpQkJoaU56RURnT3c3Tlc1ZmZHNElLTEtOSmVIOFQxdjJNIn0.eyJleHAiOjE2OTkyNzU0NjEsImlhdCI6MTY5OTI3NTE2MSwianRpIjoiZjE4YWE4ZjEtMmRlYy00YjA1LThjMDktYjRhYTQ2NjAxNzQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg0L3JlYWxtcy9XYWxsZXRJZFAiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOWY3ZDVhNGUtYTE4ZS00YzRjLThlNTYtYjE0MDBhNmFiNjllIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoid2FsbGV0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJhMjA5YWMyMi04MDkzLTQ0NGYtODk1Yy0yZjY1NzZjMjU2ZDkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy13YWxsZXRpZHAiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiYTIwOWFjMjItODA5My00NDRmLTg5NWMtMmY2NTc2YzI1NmQ5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqaWFibzAxMiIsImVtYWlsIjoiamlhYm8wMTJAZ21haWwuY29tIn0.3lDZe7QKXajP_2Fflf4IyDHrhVgMfjJN2twCf7g2UUcRRO7tTK0w9kSV_SKI1GF2aptAJ32NB4AZbtcTF8MG9WK5uC8P-wBKYxYFuw_9GCD1ddFN6fCAqNKuRHEJ7kwJ_5PIuSHvp6GfPGNGcGo2u0KTZJyOZ7BNU5iBibNFXQiqSp4hAYz982tvqW3ogM4vq64g32ljnLXdYI4KMLAxB5qxfTKbAd_WkUtGEMCGGACAtN1IDSZaW0FL-q4qSYjcq6NOIf6kIRz9bq0IQ0QFW0f-BrzWw4SN-bAFGzfR8FdZOuU6AtPLX0Y9y730bb4OgwEicAWmcsuVjFs3ML6uVQ"
        every {
            applicationUtils.postRequest(any(), any(), any())
        } returns "mockedResponse"

        // Call the method
        val result = service.processQrContent(qrContent, token)

        // Assert
        Assertions.assertEquals("mockedResponse", result)
    }

    @Test
    fun testProcessSiopAuthenticationRequestUri() {

        // Mock behavior
        val qrContent = "http://portal-api:8087/api/verifier/authentication-requests?state=Np1uP6n3TmSDBMj3m-Rx3w"
        val token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxOGFyVmZaZTJpQkJoaU56RURnT3c3Tlc1ZmZHNElLTEtOSmVIOFQxdjJNIn0.eyJleHAiOjE2OTkyNzU0NjEsImlhdCI6MTY5OTI3NTE2MSwianRpIjoiZjE4YWE4ZjEtMmRlYy00YjA1LThjMDktYjRhYTQ2NjAxNzQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg0L3JlYWxtcy9XYWxsZXRJZFAiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOWY3ZDVhNGUtYTE4ZS00YzRjLThlNTYtYjE0MDBhNmFiNjllIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoid2FsbGV0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJhMjA5YWMyMi04MDkzLTQ0NGYtODk1Yy0yZjY1NzZjMjU2ZDkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy13YWxsZXRpZHAiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiYTIwOWFjMjItODA5My00NDRmLTg5NWMtMmY2NTc2YzI1NmQ5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqaWFibzAxMiIsImVtYWlsIjoiamlhYm8wMTJAZ21haWwuY29tIn0.3lDZe7QKXajP_2Fflf4IyDHrhVgMfjJN2twCf7g2UUcRRO7tTK0w9kSV_SKI1GF2aptAJ32NB4AZbtcTF8MG9WK5uC8P-wBKYxYFuw_9GCD1ddFN6fCAqNKuRHEJ7kwJ_5PIuSHvp6GfPGNGcGo2u0KTZJyOZ7BNU5iBibNFXQiqSp4hAYz982tvqW3ogM4vq64g32ljnLXdYI4KMLAxB5qxfTKbAd_WkUtGEMCGGACAtN1IDSZaW0FL-q4qSYjcq6NOIf6kIRz9bq0IQ0QFW0f-BrzWw4SN-bAFGzfR8FdZOuU6AtPLX0Y9y730bb4OgwEicAWmcsuVjFs3ML6uVQ"

        // Call the method
        val result = service.processQrContent(qrContent, token)
    }

    @Test
    fun testProcessSiopAuthenticationRequest() {

        // Mock behavior
        val qrContent = "openid://?scope=[LEARCredential]&response_type=vp_token&response_mode=direct_post&client_id=did:key:zDnaewAy42NRd9Bes683kjXaemcYjkjCLa3xcrYd1SptMvrah&state=VmAQEAp2RZS1CvnsKdt-wA&nonce=85fd318a-82e5-42a6-a099-45a326ac7221&redirect_uri=http://portal-api:8087/api/verifier/siop-sessions"
        val token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxOGFyVmZaZTJpQkJoaU56RURnT3c3Tlc1ZmZHNElLTEtOSmVIOFQxdjJNIn0.eyJleHAiOjE2OTkyNzU0NjEsImlhdCI6MTY5OTI3NTE2MSwianRpIjoiZjE4YWE4ZjEtMmRlYy00YjA1LThjMDktYjRhYTQ2NjAxNzQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg0L3JlYWxtcy9XYWxsZXRJZFAiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOWY3ZDVhNGUtYTE4ZS00YzRjLThlNTYtYjE0MDBhNmFiNjllIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoid2FsbGV0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJhMjA5YWMyMi04MDkzLTQ0NGYtODk1Yy0yZjY1NzZjMjU2ZDkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy13YWxsZXRpZHAiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiYTIwOWFjMjItODA5My00NDRmLTg5NWMtMmY2NTc2YzI1NmQ5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqaWFibzAxMiIsImVtYWlsIjoiamlhYm8wMTJAZ21haWwuY29tIn0.3lDZe7QKXajP_2Fflf4IyDHrhVgMfjJN2twCf7g2UUcRRO7tTK0w9kSV_SKI1GF2aptAJ32NB4AZbtcTF8MG9WK5uC8P-wBKYxYFuw_9GCD1ddFN6fCAqNKuRHEJ7kwJ_5PIuSHvp6GfPGNGcGo2u0KTZJyOZ7BNU5iBibNFXQiqSp4hAYz982tvqW3ogM4vq64g32ljnLXdYI4KMLAxB5qxfTKbAd_WkUtGEMCGGACAtN1IDSZaW0FL-q4qSYjcq6NOIf6kIRz9bq0IQ0QFW0f-BrzWw4SN-bAFGzfR8FdZOuU6AtPLX0Y9y730bb4OgwEicAWmcsuVjFs3ML6uVQ"
        every {
            applicationUtils.postRequest(any(), any(), any())
        } returns "mockedResponse"

        // Call the method
        val result = service.processQrContent(qrContent, token)

        // Assert
        Assertions.assertEquals("mockedResponse", result)
    }

    @Test
    fun testProcessQrContentUnknown() {
        // Call the method with unknown QR content
        val qrContent = "unknown-content"
        val token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxOGFyVmZaZTJpQkJoaU56RURnT3c3Tlc1ZmZHNElLTEtOSmVIOFQxdjJNIn0.eyJleHAiOjE2OTkyNzU0NjEsImlhdCI6MTY5OTI3NTE2MSwianRpIjoiZjE4YWE4ZjEtMmRlYy00YjA1LThjMDktYjRhYTQ2NjAxNzQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg0L3JlYWxtcy9XYWxsZXRJZFAiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiOWY3ZDVhNGUtYTE4ZS00YzRjLThlNTYtYjE0MDBhNmFiNjllIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoid2FsbGV0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiJhMjA5YWMyMi04MDkzLTQ0NGYtODk1Yy0yZjY1NzZjMjU2ZDkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy13YWxsZXRpZHAiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiYTIwOWFjMjItODA5My00NDRmLTg5NWMtMmY2NTc2YzI1NmQ5IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqaWFibzAxMiIsImVtYWlsIjoiamlhYm8wMTJAZ21haWwuY29tIn0.3lDZe7QKXajP_2Fflf4IyDHrhVgMfjJN2twCf7g2UUcRRO7tTK0w9kSV_SKI1GF2aptAJ32NB4AZbtcTF8MG9WK5uC8P-wBKYxYFuw_9GCD1ddFN6fCAqNKuRHEJ7kwJ_5PIuSHvp6GfPGNGcGo2u0KTZJyOZ7BNU5iBibNFXQiqSp4hAYz982tvqW3ogM4vq64g32ljnLXdYI4KMLAxB5qxfTKbAd_WkUtGEMCGGACAtN1IDSZaW0FL-q4qSYjcq6NOIf6kIRz9bq0IQ0QFW0f-BrzWw4SN-bAFGzfR8FdZOuU6AtPLX0Y9y730bb4OgwEicAWmcsuVjFs3ML6uVQ"

        val exception = Assert.assertThrows(NoSuchQrContentException::class.java) {
            service.processQrContent(qrContent, token)
        }

        // Verify behavior and assertions
        Assertions.assertEquals("The received QR content cannot be processed", exception.message)
    }

}