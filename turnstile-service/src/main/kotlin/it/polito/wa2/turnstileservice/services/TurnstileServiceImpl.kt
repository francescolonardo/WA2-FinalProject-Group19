package it.polito.wa2.turnstileservice.services

import it.polito.wa2.turnstileservice.dtos.*
import it.polito.wa2.turnstileservice.entities.TurnstileDetails
import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import it.polito.wa2.turnstileservice.exceptions.TurnstileException
import it.polito.wa2.turnstileservice.repositories.TurnstileRepository
import it.polito.wa2.turnstileservice.repositories.TurnstileValidationRepository
import it.polito.wa2.turnstileservice.security.JwtUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
        ticketQrDTO: TicketQrDTO,
        loggedTurnstileId: Long,
        authorizationHeader: String
    ): Boolean {
        val ticketJwt = ticketQrDTO.decodeQRCode()
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
                        this.ticketSub = ticketDTO.sub
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

    override suspend fun getTurnstileValidationByTicketId(ticketId: Long): TurnstileValidationDTO? {
        return withContext(Dispatchers.IO) {
            turnstileValidationRepository.getTurnstileValidationByTicketId(ticketId)
        }.firstOrNull()?.toDTO()
    }

    override suspend fun getTurnstileTransitCount(turnstileId: Long): TurnstileActivityDTO {
        return withContext(Dispatchers.IO) {
            TurnstileActivityDTO().apply{
                this.turnstileId = turnstileId
                this.count = turnstileValidationRepository.getTurnstileTransitCount(turnstileId).firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getAllTurnstilesTransitCount(): TurnstileActivityDTO {
        return withContext(Dispatchers.IO) {
            TurnstileActivityDTO().apply {
                this.turnstileId = null
                this.count = turnstileValidationRepository.getAllTurnstilesTransitCount().firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getTurnstileTransitCountPeriod(turnstileId: Long, startPeriod: LocalDateTime, endPeriod: LocalDateTime): TurnstileActivityDTO {
        return withContext(Dispatchers.IO) {
            TurnstileActivityDTO().apply{
                this.turnstileId = turnstileId
                this.count = turnstileValidationRepository.getTurnstileTransitCountPeriod(turnstileId,startPeriod,endPeriod).firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getAllTurnstilesTransitCountPeriod(startPeriod: LocalDateTime, endPeriod: LocalDateTime): TurnstileActivityDTO {
        return withContext(Dispatchers.IO) {
            TurnstileActivityDTO().apply{
                this.turnstileId = null
                this.count = turnstileValidationRepository.getAllTurnstilesTransitCountPeriod(startPeriod,endPeriod).firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getUserTransitCount(username: String): UserActivityDTO{
        return withContext(Dispatchers.IO) {
            UserActivityDTO().apply{
                this.username = username
                this.count = turnstileValidationRepository.getUserTransitCount(username).firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getUserTransitCountPeriod(username: String, startPeriod: LocalDateTime, endPeriod: LocalDateTime): UserActivityDTO{
        return withContext(Dispatchers.IO) {
            UserActivityDTO().apply{
                this.username = username
                this.count = turnstileValidationRepository.getUserTransitCountPeriod(username,startPeriod,endPeriod).firstOrNull() ?: 0
            }
        }
    }

    override suspend fun getAllUserTransits(username: String): Flow<TurnstileValidationDTO?> {
       return withContext(Dispatchers.IO) {
           turnstileValidationRepository.getAllUserTransits(username).map { e -> e?.toDTO() }
       }
    }
}
