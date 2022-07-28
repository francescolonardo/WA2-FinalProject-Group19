package it.polito.wa2.login_service.controllers

import it.polito.wa2.login_service.dtos.TurnstileDTO
import it.polito.wa2.login_service.dtos.TurnstileOutputDTO
import it.polito.wa2.login_service.services.TurnstileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/turnstile/")
class TurnstileController {
    @Autowired
    private lateinit var turnstileService: TurnstileService

    @PostMapping("/register")
    fun registerTurnstile(
        @RequestBody turnstileDTO: TurnstileDTO,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileOutputDTO> {
        return try {
            val turnstileOutputDTO = turnstileService.registerTurnstile(turnstileDTO.secret)
            ResponseEntity.status(HttpStatus.CREATED).body(turnstileOutputDTO)
        } catch (ex: Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/token")
    fun generateToken(@RequestBody turnstileDTO: TurnstileDTO): ResponseEntity<Any> {
        return try {
            val token = turnstileService.generateToken(turnstileDTO)
            val map = HashMap<String, String>()
            map["authorization"] = token
            ResponseEntity.status(HttpStatus.OK).body(map)
        } catch (ex: Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }
}
