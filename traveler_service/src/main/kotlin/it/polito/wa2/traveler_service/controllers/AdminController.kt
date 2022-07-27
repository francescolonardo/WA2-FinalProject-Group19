package it.polito.wa2.traveler_service.controllers

import it.polito.wa2.traveler_service.dtos.TicketPurchasedDTO
import it.polito.wa2.traveler_service.dtos.UserDetailsDTO
import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    private lateinit var travelerService: TravelerServiceImpl

    @GetMapping("/travelers", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getTravelers(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<List<UserDetailsDTO>?> {
        val retrievedProfiles = travelerService.getAllProfiles()
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfiles)
    }

    @GetMapping("/traveler/{userId}/profile")
    fun getProfile(
        @PathVariable("userId") userId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<UserDetailsDTO?> {
        val retrievedProfile = travelerService.getProfileById(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfile)
    }

    @GetMapping("/traveler/{userId}/tickets", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getTickets(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable("userId") userId: Long
    ): ResponseEntity<List<TicketPurchasedDTO?>> {
        val retrievedTickets = travelerService.getTicketsById(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedTickets)
    }
}
