package it.polito.wa2.login_service.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.mail.MessagingException

@Service
class EmailServiceImpl : EmailService {
    @Autowired
    private lateinit var mailSender: JavaMailSender

    //private val fromEmail = "noreply@ticketservice.com"
    private val fromEmail = "wa2.group19@gmail.com"
    private val emailSubject = "Confirmation instructions"

    private fun getEmailBody(username: String, provisionalId: String, activationCode: String): String {
        val confirmationLink = "http://localhost:8080/user/validate/" +
                "provisional_id=$provisionalId&" +
                "activation_code=$activationCode"
        return "<h2>Welcome $username!</h2>" +
                "<h4>You are almost in, you just need to confirm your account email</h4>" +
                "<a href=$confirmationLink>confirm account</a>"
    }

    @Async
    override fun sendEmail(toUsername: String, toEmail: String, provisionalId: String, activationCode: String) {
        try {
            val mimeMessage = mailSender.createMimeMessage()
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, "utf-8")
            mimeMessageHelper.setFrom(fromEmail)
            mimeMessageHelper.setTo(toEmail)
            mimeMessageHelper.setSubject(emailSubject)
            mimeMessageHelper.setText(getEmailBody(toUsername, provisionalId, activationCode), true)
            mailSender.send(mimeMessage)
            println("Email to $toEmail successfully sent!")
        }
        catch (ex: MessagingException) {
            println(ex.localizedMessage)
        }
    }

}
