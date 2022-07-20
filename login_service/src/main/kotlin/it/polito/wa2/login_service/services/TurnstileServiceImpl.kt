package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.TurnstileOutputDTO
import it.polito.wa2.login_service.entities.Turnstile
import it.polito.wa2.login_service.repositories.TurnstileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TurnstileServiceImpl: TurnstileService {
    @Autowired
    private lateinit var turnstileRepository: TurnstileRepository

    private lateinit var errorExplanation: String

    private fun checkPasswordStrength(password: String): Boolean {
        val passwordRegex = Regex(
            "^" +
                    "(?=.*[a-z])" +         // at least 1 lowercase letter
                    "(?=.*[A-Z])" +         // at least 1 uppercase letter
                    "(?=.*[0-9])" +         // at least 1 digit
                    "(?=.*[@#$%^&+=])" +    // at least 1 special character
                    "(?=\\S+$)" +           // no whitespaces
                    ".{8,}" +               // at least 8 characters
                    "$"
        )
        return passwordRegex.matches(password)
    }

    override fun registerTurnstile(secret: String): TurnstileOutputDTO {
        if(!checkPasswordStrength(secret))
            throw java.lang.Exception("Password not strong enough")

        val newTurnstile = turnstileRepository.save(
            Turnstile().apply {
                this.secret = secret
            }
        )

        return TurnstileOutputDTO(newTurnstile.id)
    }
}