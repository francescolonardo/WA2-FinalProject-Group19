package it.polito.wa2.turnstileservice.controllers

import it.polito.wa2.turnstileservice.dtos.TicketQrDTO
import it.polito.wa2.turnstileservice.services.TurnstileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/turnstile/")
class TurnstileController {
    @Autowired
    lateinit var turnstileService: TurnstileService

    @PostMapping("/validate")
    suspend fun validateTicketPost(
        @RequestBody ticketQR: TicketQrDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedTurnstile: Principal
    ): ResponseEntity<Boolean> {
        return if(!turnstileService.validateTicket(ticketQR, loggedTurnstile.name.toLong(), authorizationHeader))
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
        else
            ResponseEntity.ok(true)
    }
}
