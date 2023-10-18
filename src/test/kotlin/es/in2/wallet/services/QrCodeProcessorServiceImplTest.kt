package es.in2.wallet.services

import es.in2.wallet.api.exception.NoSuchQrContentException
import es.in2.wallet.api.service.impl.QrCodeProcessorServiceImpl
import es.in2.wallet.api.utils.ApplicationUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class QrCodeProcessorServiceImplTest {

    private val applicationUtils: ApplicationUtils = mockk(relaxed = true)

    private val service: QrCodeProcessorServiceImpl = mockk()

    @Test
    fun testProcessQrContentCredentialOfferUri() {

        // Mock behavior
        val qrContent = "https://www.goodair.com/credential-offer?credential_offer_uri=https://www.goodair.com/credential-offer/5j349k3e3n23j"

        every {
            applicationUtils.postRequest(any(), any(), any())
        } returns "mockedResponse"

        every {
            service.processQrContent(qrContent)
        } returns "mockedResponse"


        // Call the method
        val result = service.processQrContent(qrContent)

        // Assert
        Assertions.assertEquals("mockedResponse", result)

        // Verify
        verify(exactly = 1) {
            // Use the mock object for verification
            service.processQrContent(any())
        }

    }

    @Test
    fun testProcessQrContentUnknown() {
        // Call the method with unknown QR content
        val qrContent = "unknown-content"

        // Call the method
        val exception = Assert.assertThrows(NoSuchQrContentException::class.java) {
            service.processQrContent(qrContent)
        }
        // Verify behavior and assertions
        Assertions.assertEquals("The received QR content cannot be processed", exception.message)
    }

}