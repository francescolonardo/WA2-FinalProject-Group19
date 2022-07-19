package it.polito.wa2.ticket_catalogue_service.controllers

import it.polito.wa2.ticket_catalogue_service.dtos.TicketDTO
import it.polito.wa2.ticket_catalogue_service.services.TicketServiceImpl
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController {
    @Autowired
    private lateinit var ticketService: TicketServiceImpl

    @GetMapping(produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getTickets(): ResponseEntity<Flow<TicketDTO>> {
        val retrievedTickets = ticketService.getAllTickets()
        return ResponseEntity.ok(retrievedTickets)
    }
}
