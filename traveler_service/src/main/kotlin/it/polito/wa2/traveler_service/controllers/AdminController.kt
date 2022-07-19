package it.polito.wa2.traveler_service.controllers

import it.polito.wa2.traveler_service.dtos.TicketPurchasedDTO
import it.polito.wa2.traveler_service.dtos.UserDetailsDTO
import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    private lateinit var travelerService: TravelerServiceImpl

    @GetMapping("/travelers")
    fun getTravelers(): ResponseEntity<List<UserDetailsDTO>?> {
        val retrievedProfiles = travelerService.getAllProfiles()
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfiles)
    }

    @GetMapping("/traveler/{userID}/profile")
    fun getProfile(@PathVariable("userID") userID: Long): ResponseEntity<UserDetailsDTO?> {
        val retrievedProfile = travelerService.getProfileById(userID)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfile)
    }

    @GetMapping("/traveler/{userID}/tickets")
    fun getTickets(@PathVariable("userID") userID: Long): ResponseEntity<List<TicketPurchasedDTO?>> {
        val retrievedTickets = travelerService.getTicketsById(userID)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedTickets)
    }
}
