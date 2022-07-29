package it.polito.wa2.turnstileservice.services

import it.polito.wa2.turnstileservice.dtos.*
import it.polito.wa2.turnstileservice.entities.TurnstileDetails
import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import it.polito.wa2.turnstileservice.exceptions.TurnstileException
import it.polito.wa2.turnstileservice.repositories.TurnstileRepository
import it.polito.wa2.turnstileservice.repositories.TurnstileValidationRepository
import it.polito.wa2.turnstileservice.security.JwtUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDateTime

@Service
class TurnstileServiceImpl: TurnstileService {
    private val travelerWebClient = WebClient.create("http://localhost:8282") // traveler-service
    @Autowired
    lateinit var turnstileRepository: TurnstileRepository
    @Autowired
    lateinit var turnstileValidationRepository: TurnstileValidationRepository
    @Value("\${jwt.tickets.signature-key-base64}")
    private lateinit var jwtTicketsSecretB64Key: String
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String

    private suspend fun setTicketAsUsed(ticketId: Long, authorizationHeader: String): Boolean {
        val ticketUpdated: Boolean = travelerWebClient
            .put()
            .uri("/turnstile/validate/${ticketId}")
            .accept(MediaType.APPLICATION_JSON)
            .header(jwtHttpHeaderName, authorizationHeader)
            .retrieve()
            .onStatus({ !it.equals(HttpStatus.OK) }) { resp ->
                resp.bodyToMono(String::class.java).map { Exception(it) }
            }
            .awaitBody()
        return ticketUpdated
    }

    override suspend fun validateTicket(
        loggedTurnstileId: Long,
        ticketQRDTO: TicketQRDTO,
        authorizationHeader: String
    ): Boolean {
        val ticketJwt = ticketQRDTO.decodeQRCode()
            ?: return false
        val jwtUtils = JwtUtils(jwtTicketsSecretB64Key)
        val validation = jwtUtils.validateJwt(ticketJwt)
        if (!validation)
            return false
        else {
            val ticketDTO: TicketDTO = jwtUtils.getDetailsJwtTicket(ticketJwt)
            try {
                val ticketUpdated = setTicketAsUsed(ticketDTO.sub, authorizationHeader)
                if (!ticketUpdated)
                    return false
                turnstileValidationRepository.save(
                    TurnstileValidation().apply {
                        this.turnstileId = loggedTurnstileId
                        this.ticketId = ticketDTO.sub
                        this.username = ticketDTO.username
                        this.dateTime = LocalDateTime.now()
                    }
                )
                return true
            } catch (ex: Exception) {
                println(ex.localizedMessage)
                return false
            }
        }
    }

    override suspend fun getTurnstileDetails(
        turnstileId: Long
    ): TurnstileDetailsDTO? {
        return turnstileRepository.findById(turnstileId)
            ?.toDTO()
    }

    override suspend fun addTurnstileDetails(
        turnstileDetailsDTO: TurnstileDetailsDTO
    ): TurnstileDetailsDTO {
        val retrievedTurnstileDetails =
            turnstileRepository.findByTurnstileId(turnstileDetailsDTO.turnstileId)
        if (retrievedTurnstileDetails != null)
            throw TurnstileException("zid already assigned to this turnstile")
        return turnstileRepository.save(
            TurnstileDetails().apply {
                this.turnstileId = turnstileDetailsDTO.turnstileId
                this.zid = turnstileDetailsDTO.zid
            }
        ).toDTO()
    }

    override suspend fun getTurnstileValidationsByTurnstileId(
        turnstileId: Long
    ): Flow<TurnstileValidationDTO> {
        return turnstileValidationRepository.findByTurnstileId(turnstileId)
            .map { turnstileValidation -> turnstileValidation.toDTO() }
    }

    override suspend fun getTurnstilesValidationByTicketId(
        ticketId: Long
    ): TurnstileValidationDTO? {
        return turnstileValidationRepository.findByTicketId(ticketId)
            ?.toDTO()
    }

    override suspend fun getAllTurnstilesActivity(): Long {
        return turnstileValidationRepository.count()
    }

    override suspend fun getTurnstileActivity(turnstileId: Long): Long {
        return turnstileValidationRepository.countByTurnstileId(turnstileId)
    }

    override suspend fun getAllTurnstilesActivityPeriod(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Long {
        return turnstileValidationRepository.countByDateTimeBetween(startDate, endDate)
    }

    override suspend fun getTurnstileActivityPeriod(
        turnstileId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Long {
        return turnstileValidationRepository.countByTurnstileIdAndDateTimeBetween(turnstileId, startDate, endDate)
    }

    override suspend fun getUserActivity(username: String): Long {
        return turnstileValidationRepository.countByUsername(username)
    }

    override suspend fun getUserActivityPeriod(
        username: String,
        startPeriod: LocalDateTime,
        endPeriod: LocalDateTime
    ): Long {
        return turnstileValidationRepository.countByUsernameAndDateTimeBetween(username,startPeriod,endPeriod)
    }

    override fun getAllUserTransits(username: String): Flow<TurnstileValidationDTO?> {
        return turnstileValidationRepository.findAllByUsername(username)
            .map { turnstileValidation -> turnstileValidation?.toDTO() }
    }
}
