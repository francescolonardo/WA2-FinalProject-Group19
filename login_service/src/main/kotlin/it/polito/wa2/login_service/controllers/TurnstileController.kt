package it.polito.wa2.login_service.controllers

import it.polito.wa2.login_service.dtos.TurnstileDTO
import it.polito.wa2.login_service.dtos.TurnstileOutputDTO
import it.polito.wa2.login_service.services.TurnstileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/turnstile/")
class TurnstileController {
    @Autowired
    private lateinit var turnstileService: TurnstileService

    @PostMapping("/register")
    fun registerTurnstile(@RequestBody turnstileDTO: TurnstileDTO) : ResponseEntity<TurnstileOutputDTO> {
        return try {
            val turnstileOutputDTO = turnstileService.registerTurnstile(turnstileDTO.secret)
            ResponseEntity.status(HttpStatus.CREATED).body(turnstileOutputDTO)
        }catch (ex: Exception){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }
}