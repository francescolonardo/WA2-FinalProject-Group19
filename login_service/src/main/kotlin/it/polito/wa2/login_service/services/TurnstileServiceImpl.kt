package it.polito.wa2.login_service.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.login_service.dtos.TurnstileDTO
import it.polito.wa2.login_service.dtos.TurnstileOutputDTO
import it.polito.wa2.login_service.entities.Role
import it.polito.wa2.login_service.entities.Turnstile
import it.polito.wa2.login_service.repositories.TurnstileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class TurnstileServiceImpl: TurnstileService {
    @Autowired
    private lateinit var turnstileRepository: TurnstileRepository

    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.authorization.expiration-time-ms}")
    private lateinit var jwtExpirationTimeMs: String

    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

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
            throw Exception("Password not strong enough")

        val newTurnstile = turnstileRepository.save(
            Turnstile().apply {
                this.secret = bCryptPasswordEncoder.encode(secret)
            }
        )

        return TurnstileOutputDTO(newTurnstile.id)
    }

    override fun generateToken(turnstileDTO: TurnstileDTO): String {
        val turnstile = turnstileRepository.findById(turnstileDTO.id)
        if(turnstile.isEmpty)
            throw Exception("Turnstile not found")

        if(!bCryptPasswordEncoder.matches(turnstileDTO.secret, turnstile.get().secret))
            throw Exception("Wrong secret")

        val jwtSecretByteKey = Base64.getDecoder().decode(jwtSecretB64Key)
        val jwtSecretKey: Key = Keys.hmacShaKeyFor(jwtSecretByteKey)
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(turnstileDTO.id.toString())
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationTimeMs.toInt()))
            .claim("roles", mutableSetOf(Role.EMBEDDED))
            .signWith(jwtSecretKey)
            .compact()
    }
}