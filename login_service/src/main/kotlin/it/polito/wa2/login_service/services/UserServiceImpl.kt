package it.polito.wa2.login_service.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.login_service.dtos.ActivationOutputDTO
import it.polito.wa2.login_service.dtos.AuthorizationTokenDTO
import it.polito.wa2.login_service.dtos.UserOutputDTO
import it.polito.wa2.login_service.entities.Activation
import it.polito.wa2.login_service.entities.User
import it.polito.wa2.login_service.exceptions.InvalidActivationException
import it.polito.wa2.login_service.exceptions.InvalidUserException
import it.polito.wa2.login_service.exceptions.LoginException
import it.polito.wa2.login_service.repositories.ActivationRepository
import it.polito.wa2.login_service.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDate
import java.util.*

@Service
class UserServiceImpl : UserService {
    @Autowired
    private lateinit var emailService: EmailService
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var activationRepository: ActivationRepository
    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    private lateinit var errorExplanation: String

    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.authorization.expiration-time-ms}")
    private var jwtExpirationTimeMs: Int = 0

    private fun checkEmailFormat(email: String): Boolean {
        val emailRegex = Regex(
            "^" +
            "[a-zA-Z0-9._\\-]{4,16}" +
            "@" +
            "[a-zA-Z0-9\\-]{2,8}" +
            "(" +
                "\\." +
                "[a-zA-Z0-9]{2,8}" +
            ")" +
            "$"
        )
        val emailCheck = emailRegex.matches(email)
        if (!emailCheck)
            errorExplanation = "wrong email format"
        return emailCheck
    }

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
        val passwordCheck = passwordRegex.matches(password)
        if (!passwordCheck)
            errorExplanation = "password not strong enough"
        return passwordCheck
    }

    private fun checkUser(username: String, password: String, email: String) {
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            if (checkEmailFormat(email) && checkPasswordStrength(password)) {
                if (userRepository.getUserByUsernameOrEmail(username, email) == null) {
                    return
                }
                errorExplanation = "user already exists"
            }
        }
        else
            errorExplanation = "fields cannot be empty"
        throw InvalidUserException("User not valid: $errorExplanation")
    }

    private fun getRandomActivationCode(length: Int = 6): String {
        return (1..length)
            .map { "0123456789".random() }
            .joinToString("")
    }

    /*
     * checking that the submitted user's properties are suitable
     *
     * if data (username, password, email) pass the checks,
     * a new user and a new activation are saved in the db,
     * then it returns an object containing
     * the new activation's provisional id and the new user's email,
     * otherwise it throws an InvalidUserException
     */
    override fun registerUser(username: String, password: String, email: String): ActivationOutputDTO {
        checkUser(username, password, email)
        val newUser = userRepository.save(
            User(
                username,
                passwordEncoder.encode(password),
                email
            )
        )
        val newActivation = activationRepository.save(
            Activation().apply {
                activationCode = getRandomActivationCode()
                user = newUser
            }
        )
        emailService.sendEmail(
            username, email,
            newActivation.provisionalId.toString(), newActivation.activationCode
        )
        return ActivationOutputDTO(
            newActivation.provisionalId,
            newActivation.user!!.email
        )
    }

    private fun decrementAttemptCounter(attemptCounter: Int, provisionalId: UUID, userId: Long) {
        if (attemptCounter > 1)
            activationRepository.decrementAttemptCounterByProvisionalId(provisionalId)
        else {
            // it's no longer necessary to delete activations (because of ON DELETE CASCADE)
            //activationRepository.deleteActivationByProvisionalId(provisionalId)
            userRepository.deleteById(userId)
        }
    }

    private fun checkActivation(provisionalId: UUID, activationCode: String): User {
        val retrievedActivation = activationRepository.getActivationByProvisionalId(provisionalId)
        if (retrievedActivation != null && retrievedActivation.deadline.isAfter(LocalDate.now())) {
            if (retrievedActivation.user != null) {
                if (Regex("[0-9]{6}").matches(activationCode)) {
                    if (activationCode == retrievedActivation.activationCode) {
                        return retrievedActivation.user!!
                    }
                }
                errorExplanation = "wrong activation code"
                decrementAttemptCounter(
                    retrievedActivation.attemptCounter,
                    retrievedActivation.provisionalId,
                    retrievedActivation.user!!.id
                )
            }
        }
        else
            errorExplanation = "activation not found or expired"
        throw InvalidActivationException("Activation not valid: $errorExplanation")
    }

    /*
     * checking that the submitted activation's properties are suitable
     *
     * if data (provisionalId, activationCode) pass the checks,
     * the pending activation is removed from the db and the correlated user is set to active,
     * then it returns an object containing the activated user's id, username, email,
     * otherwise it decrements the existing activation's attempts counter
     * (and if its value reaches the zero, both the existing user
     * and the correlated activation are removed from the db)
     * and throws an InvalidActivationException
     */
    override fun validateUser(provisionalId: UUID, activationCode: String): UserOutputDTO {
        val retrievedUser = checkActivation(provisionalId, activationCode)
        activationRepository.deleteActivationByProvisionalId(provisionalId)
        userRepository.setActiveById(retrievedUser.id)
        return UserOutputDTO(
            retrievedUser.id,
            retrievedUser.username,
            retrievedUser.email
        )
    }

    override fun loginUser(username: String, password: String): AuthorizationTokenDTO {
        val retrievedUser = userRepository.findByUsername(username)
        if (retrievedUser == null ||
            !passwordEncoder.matches(password, retrievedUser.password) ||
            retrievedUser.active == 0) // we need that the user is active to login // TODO: change to 0
            throw LoginException("Login rejected")
        val jwtSecretByteKey = Base64.getDecoder().decode(jwtSecretB64Key)
        val jwtSecretKey: Key = Keys.hmacShaKeyFor(jwtSecretByteKey)
        val accessToken = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationTimeMs))
            .claim("roles", retrievedUser.roles)
            .signWith(jwtSecretKey)
            .compact()
        return AuthorizationTokenDTO(accessToken)
    }
}
