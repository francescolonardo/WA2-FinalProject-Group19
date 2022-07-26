package it.polito.wa2.login_service.controllers

import it.polito.wa2.login_service.dtos.AdminDTO
import it.polito.wa2.login_service.dtos.AdminOutputDTO
import it.polito.wa2.login_service.exceptions.DisableAccountException
import it.polito.wa2.login_service.exceptions.EnrollingCapabilityException
import it.polito.wa2.login_service.exceptions.InvalidUserException
import it.polito.wa2.login_service.services.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.HashMap

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    private lateinit var userService: UserServiceImpl

    @PostMapping("/enrolling")
    fun adminEnrolling(
        @RequestBody adminDTO: AdminDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<AdminOutputDTO?> {
        try {
            val adminOutputDTO = userService.enrollAdmin(
                adminDTO.username,
                adminDTO.password,
                adminDTO.enrollingCapability
            )
            return ResponseEntity.status(HttpStatus.CREATED).body(adminOutputDTO)
        } catch (ex: EnrollingCapabilityException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        }
        catch (ex: InvalidUserException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @GetMapping("/disableAccount/{userId}")
    fun adminDisableAccount(
        @PathVariable("userId") userId: Long,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<Any> {
        try {
            userService.disableAccountAdmin(loggedUser.name, userId)
            return ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: EnrollingCapabilityException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        } catch (ex: DisableAccountException) {
            println(ex.localizedMessage)
            val error = HashMap<String, String>()
            error["error"] = ex.localizedMessage
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
        }
    }
}
