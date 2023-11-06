package es.in2.wallet.api.service

fun interface QrCodeProcessorService {
    fun processQrContent(qrContent: String, token: String): Any
}


