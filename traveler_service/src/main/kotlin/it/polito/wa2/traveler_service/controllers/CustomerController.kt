package it.polito.wa2.traveler_service.controllers

import it.polito.wa2.traveler_service.dtos.*
import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Integer.parseInt
import java.security.Principal
import java.text.SimpleDateFormat

@RestController
@RequestMapping("/my/")
class CustomerController {
    @Autowired
    private lateinit var travelerService: TravelerServiceImpl

    @GetMapping("/profile")
    fun getProfile(
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<UserDetailsDTO?> {
        val retrievedProfile = travelerService.getProfileByUsername(loggedUser.name)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedProfile)
    }

    @PutMapping("/profile")
    fun putProfile(
        @RequestBody userDetailsInputDTO: UserDetailsInputDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<UserDetailsDTO?> {
        val dateOfBirthString: String
        try {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val dateOfBirth = dateFormatter.parse(userDetailsInputDTO.dateOfBirth)
            dateOfBirthString = dateFormatter.format(dateOfBirth)
            parseInt(userDetailsInputDTO.telephoneNumber)
            if (userDetailsInputDTO.telephoneNumber.length < 9 ||
                userDetailsInputDTO.telephoneNumber.length > 10)
                throw NumberFormatException()
            if (userDetailsInputDTO.address.isEmpty())
                throw IllegalArgumentException()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
        }
        val updatedProfile = travelerService.updateProfileByUsername(
            loggedUser.name,
            dateOfBirthString,
            userDetailsInputDTO.address,
            userDetailsInputDTO.telephoneNumber
        )
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(updatedProfile)
    }

    @GetMapping("/tickets")
    fun getTickets(
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<List<TicketPurchasedDTO?>> {
        val retrievedTickets = travelerService.getTicketsByUsername(loggedUser.name)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(retrievedTickets)
    }

    @PostMapping("/tickets")
    fun postTickets(
        @RequestBody buyTicketsDTO: BuyTicketsRequestDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<List<TicketPurchasedDTO?>> {
        if (buyTicketsDTO.cmd != "buy_tickets" ||
            buyTicketsDTO.quantity < 1 ||
            buyTicketsDTO.zones.isEmpty() ||
            buyTicketsDTO.zones.length > 5) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
        }
        val zonesList = mutableSetOf<Char>()
        for (i in 0 until buyTicketsDTO.zones.length) {
            if ((buyTicketsDTO.zones[i] != 'A' &&
                buyTicketsDTO.zones[i] != 'B' &&
                buyTicketsDTO.zones[i] != 'C' &&
                buyTicketsDTO.zones[i] != 'D' &&
                buyTicketsDTO.zones[i] != 'E') ||
                !zonesList.add(buyTicketsDTO.zones[i])) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
            }
        }
        val purchasedTickets = travelerService.purchaseTicketsByUsername(
            loggedUser.name,
            buyTicketsDTO.quantity,
            buyTicketsDTO.zones
        )
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        return ResponseEntity.ok().body(purchasedTickets)
    }
    
}
