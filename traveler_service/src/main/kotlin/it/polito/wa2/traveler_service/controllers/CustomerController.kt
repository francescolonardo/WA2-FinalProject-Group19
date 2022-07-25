package it.polito.wa2.traveler_service.controllers

import it.polito.wa2.traveler_service.dtos.*
import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.lang.Integer.parseInt

@RestController
@RequestMapping("/my/")
class CustomerController {
    @Autowired
    private lateinit var travelerService: TravelerServiceImpl

    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<UserDetailsDTO?> {
        val loggedUsername: String = SecurityContextHolder.getContext().authentication.name
        val retrievedProfile = travelerService.getProfileByUsername(loggedUsername)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfile)
    }

    @PutMapping("/profile")
    fun putProfile(@RequestBody userDetailsInputDTO: UserDetailsInputDTO): ResponseEntity<UserDetailsDTO?> {
        try {
            parseInt(userDetailsInputDTO.telephoneNumber)
            if (userDetailsInputDTO.telephoneNumber.length < 9 ||
                userDetailsInputDTO.telephoneNumber.length > 10)
                throw NumberFormatException()
            if (userDetailsInputDTO.address.isEmpty())
                throw IllegalArgumentException()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
        }
        val loggedUsername: String = SecurityContextHolder.getContext().authentication.name
        val updatedProfile = travelerService.updateProfileByUsername(
            loggedUsername,
            userDetailsInputDTO.address,
            userDetailsInputDTO.telephoneNumber
        )
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(updatedProfile)
    }

    @GetMapping("/tickets")
    fun getTickets(): ResponseEntity<List<TicketPurchasedDTO?>> {
        val loggedUsername: String = SecurityContextHolder.getContext().authentication.name
        val retrievedTickets = travelerService.getTicketsByUsername(loggedUsername)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedTickets)
    }

    @PostMapping("/tickets")
    fun postTickets(@RequestBody buyTicketsDTO: BuyTicketsRequestDTO): ResponseEntity<List<TicketPurchasedDTO?>> {
        if (buyTicketsDTO.cmd != "buy_tickets" ||
            buyTicketsDTO.quantity < 1 ||
            buyTicketsDTO.zones.isEmpty() ||
            buyTicketsDTO.zones.length > 3) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
        }
        val zonesList = mutableSetOf<Char>()
        for (i in 0 until buyTicketsDTO.zones.length) {
            if ((buyTicketsDTO.zones[i] != 'A' &&
                buyTicketsDTO.zones[i] != 'B' &&
                buyTicketsDTO.zones[i] != 'C') ||
                !zonesList.add(buyTicketsDTO.zones[i])) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
            }
        }
        val loggedUsername: String = SecurityContextHolder.getContext().authentication.name
        val purchasedTickets = travelerService.purchaseTicketsByUsername(
            loggedUsername,
            buyTicketsDTO.quantity,
            buyTicketsDTO.zones
        )
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(purchasedTickets)
    }
    
    
       @PutMapping("/tickets/{id}/validate")
     fun validateUsedTickets(
        @PathVariable("id") id: Long,
    ): ResponseEntity<TicketPurchasedDTO?> {

        //first check the given id of the ticket is exist in db or not
        val retrievedTicket = travelerService.getTicketDetailById(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        // check the status of ticket (used or not)
        val status = travelerService.updateUsedPropertyById(retrievedTicket)
        return if(status)
            ResponseEntity.ok().body(retrievedTicket.toDTO())

        else
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }
    
    
    
}
