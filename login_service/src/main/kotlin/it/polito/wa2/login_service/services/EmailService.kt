package it.polito.wa2.login_service.services

interface EmailService {
    fun sendEmail(toUsername: String, toEmail: String, provisionalId: String, activationCode: String)
}
