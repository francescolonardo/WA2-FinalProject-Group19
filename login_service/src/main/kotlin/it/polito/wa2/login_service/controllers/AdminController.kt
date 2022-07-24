package it.polito.wa2.login_service.controllers

import it.polito.wa2.login_service.dtos.AdminDTO
import it.polito.wa2.login_service.dtos.AdminOutputDTO
import it.polito.wa2.login_service.exceptions.InvalidUserException
import it.polito.wa2.login_service.repositories.UserRepository
import it.polito.wa2.login_service.services.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    private lateinit var userService: UserServiceImpl
    @Autowired
    private lateinit var userRepository: UserRepository

    @PostMapping("/enrolling")
    fun adminEnrolling(
        @RequestBody adminDTO: AdminDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<AdminOutputDTO?> {
        val retrievedAdmin = userRepository.findByUsername(loggedUser.name)
        if (retrievedAdmin?.enrollingCapability == 0)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
        try {
            val adminOutputDTO = userService.enrollAdmin(
                adminDTO.username,
                adminDTO.password,
                adminDTO.enrollingCapability
            )
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminOutputDTO)
        } catch (ex: InvalidUserException) {
            println(ex.localizedMessage)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

}
