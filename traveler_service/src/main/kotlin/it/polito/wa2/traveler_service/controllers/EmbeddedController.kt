package it.polito.wa2.traveler_service.controllers

import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/embedded/")
class EmbeddedController {
    @Autowired
    private lateinit var travelerService: TravelerServiceImpl

    @PutMapping("/{ticketId}")
    fun validateUsedTickets(
        @PathVariable("ticketId") id: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Boolean> {
        val retrievedTicket = travelerService.getTicketDetailById(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false)
        val status = travelerService.updateUsedPropertyById(retrievedTicket)
        return if(status) {
            ResponseEntity.ok().body(true)
        }
        else
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false)
    }
}
