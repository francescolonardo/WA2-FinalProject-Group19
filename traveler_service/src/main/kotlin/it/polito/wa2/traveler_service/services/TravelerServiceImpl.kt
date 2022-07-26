package it.polito.wa2.traveler_service.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.traveler_service.dtos.TicketPurchasedDTO
import it.polito.wa2.traveler_service.dtos.UserDetailsDTO
import it.polito.wa2.traveler_service.dtos.toDTO
import it.polito.wa2.traveler_service.entities.Role
import it.polito.wa2.traveler_service.entities.TicketPurchased
import it.polito.wa2.traveler_service.entities.UserDetails
import it.polito.wa2.traveler_service.repositories.TicketPurchasedRepository
import it.polito.wa2.traveler_service.repositories.UserDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.sql.Timestamp
import java.util.*

@Service
class TravelerServiceImpl : TravelerService {
    @Autowired
    private lateinit var userDetailsRepository: UserDetailsRepository
    @Autowired
    private lateinit var ticketPurchasedRepository: TicketPurchasedRepository

    @Value("\${jwt.tickets.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.tickets.expiration-time-ms}")
    private var jwtExpirationTimeMs: Int = 0

    override fun getProfileById(id: Long): UserDetailsDTO? {
        return userDetailsRepository.findUserDetailsById(id)?.toDTO()
    }

    override fun getProfileByUsername(username: String): UserDetailsDTO? {
        return userDetailsRepository.findUserDetailsByUsername(username)?.toDTO()
    }

    override fun getAllProfiles(): List<UserDetailsDTO>? {
        return userDetailsRepository.getAllUserDetails()
            ?.map { profile -> profile.toDTO() }
    }

    override fun getTicketsById(id: Long): List<TicketPurchasedDTO>? {
        return userDetailsRepository.findUserDetailsById(id)
            ?.tickets?.map { ticket -> ticket.toDTO() }
    }

    override fun getTicketsByUsername(username: String): List<TicketPurchasedDTO>? {
        return userDetailsRepository.findUserDetailsByUsername(username)
            ?.tickets?.map { ticket -> ticket.toDTO() }
    }

    override fun updateProfileByUsername(username: String, dateOfBirth: String, address: String, telephoneNumber: String): UserDetailsDTO? {
        val retrievedProfile = userDetailsRepository.findUserDetailsByUsername(username)
            ?: return null
        retrievedProfile.dateOfBirth = dateOfBirth
        retrievedProfile.address = address
        retrievedProfile.telephoneNumber = telephoneNumber
        userDetailsRepository.save(retrievedProfile)
        return retrievedProfile.toDTO()
    }

    override fun purchaseTicketsByUsername(username: String, quantity: Int, zones: String): List<TicketPurchasedDTO>? {
        val retrievedProfile = userDetailsRepository.findUserDetailsByUsername(username)
            ?: return null
        val createdTickets = createTickets(username, quantity, zones)
        retrievedProfile.tickets += createdTickets
        userDetailsRepository.save(retrievedProfile)
        return createdTickets.map { createdTicket -> createdTicket.toDTO() }
    }

    fun createTickets(username: String, quantity: Int, zones: String): List<TicketPurchased> {
        val jwtSecretByteKey = Base64.getDecoder().decode(jwtSecretB64Key)
        val jwtSecretKey: Key = Keys.hmacShaKeyFor(jwtSecretByteKey)
        val iat = Timestamp(System.currentTimeMillis())
        val exp = Timestamp(System.currentTimeMillis() + jwtExpirationTimeMs)
        val tickets: MutableList<TicketPurchased> = mutableListOf()
        for (i in 1..quantity) {
            val sub = ticketPurchasedRepository.getNextSub()
            val ticket = TicketPurchased()
            ticket.iat = iat
            ticket.exp = exp
            ticket.zid = zones
            ticket.username = username
            ticket.jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(sub.toString())
                .setIssuedAt(iat)
                .setExpiration(exp)
                .claim("zid", zones)
                .claim("username", username)
                .signWith(jwtSecretKey)
                .compact()
            ticketPurchasedRepository.save(ticket)
            tickets.add(ticket)
        }
        return tickets
    }

    // TODO: remove this
    fun addUserDetails(username: String, role: Role){
        val user = UserDetails()
        user.username = username
        user.roles = setOf(role)
        userDetailsRepository.save(user)
    }
}
