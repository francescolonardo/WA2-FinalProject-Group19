package it.polito.wa2.login_service.controllers

import it.polito.wa2.login_service.dtos.*
import it.polito.wa2.login_service.exceptions.InvalidActivationException
import it.polito.wa2.login_service.exceptions.InvalidPasswordException
import it.polito.wa2.login_service.exceptions.InvalidUserException
import it.polito.wa2.login_service.exceptions.LoginException
import it.polito.wa2.login_service.services.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/user/")
class UserController {
    @Autowired
    private lateinit var userService: UserServiceImpl

    /*
     * registering a new user
     *
     * in case of success it returns an HTTP response
     * with status code 202 (accepted) and a body containing:
     * the new activation's provisional id and the correlated user's email
     * otherwise, it returns a status code 400 (bad request) and a null body
     */
    @PostMapping("/register")
    fun userRegistration(@RequestBody userDTO: TravelerDTO): ResponseEntity<ActivationOutputDTO?> {
        try {
            val activationOutputDTO = userService.registerTraveler(userDTO.username, userDTO.password, userDTO.email)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(activationOutputDTO)
        } catch (ex: InvalidUserException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    /*
     * validating the email's possession of a registered user
     *
     * in case of success it returns an HTTP response
     * with status code 201 (created) and a body containing:
     * the activated existing user's id, username, email
     * otherwise, it returns a status code 404 (not found) and a null body
     */
    @PostMapping("/validate")
    fun userValidation(@RequestBody activationDTO: ActivationDTO): ResponseEntity<TravelerOutputDTO?> {
        try {
            val travelerOutputDTO = userService.validateTraveler(
                activationDTO.provisionalId,
                activationDTO.activationCode
            )
            return ResponseEntity.status(HttpStatus.CREATED).body(travelerOutputDTO)
        } catch (ex: InvalidActivationException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    /*
     * validating the email's possession of a registered user
     *
     * like before,
     * but in this case we have a get request with two parameters
     * (used for the activation link in the email received during registration)
     */
    @GetMapping("/validate/provisional_id={provisionalId}&activation_code={activationCode}")
    fun userValidation(
        @PathVariable("provisionalId") provisionalId: UUID,
        @PathVariable("activationCode") activationCode: String
    ): String {
        try {
            val userOutputDTO = userService.validateTraveler(provisionalId, activationCode)
            return "Email ${userOutputDTO.email} confirmed, user ${userOutputDTO.username} activated!"
        } catch (ex: InvalidActivationException) {
            println(ex.localizedMessage)
            return "Validation error"
        }
    }

    /*
     * logging in the user
     *
     */
    @PostMapping("/login")
    fun userLogin(@RequestBody userLoginDTO: UserLoginDTO): ResponseEntity<AuthorizationTokenDTO> {
        try {
            val authorizationTokenDTO = userService.loginUser(userLoginDTO.username, userLoginDTO.password)
            return ResponseEntity.ok(authorizationTokenDTO)
        } catch (ex: LoginException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/changePassword")
    fun userChangePassword(
        @RequestBody changePasswordDTO: ChangePasswordDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<Any> {
        try {
            userService.changePasswordUser(
                loggedUser.name,
                changePasswordDTO.oldPassword,
                changePasswordDTO.newPassword
            )
            return ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: InvalidPasswordException) {
            val error = HashMap<String, String>()
            error["error"] = ex.localizedMessage
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
        }
    }

    @DeleteMapping("/deleteAccount")
    fun userDeleteAccount(
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<Any> {
        userService.deleteAccountTraveler(loggedUser.name)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}
