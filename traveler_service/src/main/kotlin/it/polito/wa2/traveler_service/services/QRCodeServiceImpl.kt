package it.polito.wa2.traveler_service.services

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.*

@Service
class QRCodeServiceImpl : QRCodeService {
    override fun generateQRCode(qrContent: String): String? { // returns a base64 string
        return try {
            val qrCodeWriter = QRCodeWriter()
            val bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 256, 256)
            val byteArrayOutputStream = ByteArrayOutputStream()
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream)
            Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
        } catch (ex: Exception) {
            println(ex.localizedMessage)
            null
        }
    }
}
