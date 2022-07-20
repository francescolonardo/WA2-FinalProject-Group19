package it.polito.wa2.traveler_service.services

interface QRCodeService {
    fun generateQRCode(qrContent: String): String?
}
