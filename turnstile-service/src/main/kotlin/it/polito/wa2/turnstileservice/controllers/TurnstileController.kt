package it.polito.wa2.turnstileservice.controllers

import it.polito.wa2.turnstileservice.dtos.TicketQRDTO
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
        @RequestBody ticketQRDTO: TicketQRDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedTurnstile: Principal
    ): ResponseEntity<Boolean> {
        val validationResult =
            turnstileService.validateTicket(
                1,// loggedTurnstile.name.toLong(), // TODO: fix this
                ticketQRDTO,
                authorizationHeader
            )
        return if (!validationResult)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
        else
            ResponseEntity.ok(true)
    }
}
