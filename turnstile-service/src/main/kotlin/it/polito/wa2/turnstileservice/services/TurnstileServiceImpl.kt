package it.polito.wa2.turnstileservice.services

import it.polito.wa2.turnstileservice.dtos.*
import it.polito.wa2.turnstileservice.entities.Turnstile
import it.polito.wa2.turnstileservice.entities.TurnstileValidation
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

    override suspend fun addTurnstile(turnstileDTO: TurnstileDTO): TurnstileDTO {
        return turnstileRepository.save(
            Turnstile().apply {
                this.turnstileId = turnstileDTO.turnstileId
                this.zid = turnstileDTO.zid
            }
        ).toDTO()
    }

    override suspend fun getTurnstileById(turnstileId: Long):TurnstileDTO?{
        return withContext(Dispatchers.IO) {
            val turnstile:Turnstile? = turnstileRepository.getTurnstileById(turnstileId).firstOrNull()
            val turnstileDTO: TurnstileDTO
            return@withContext if(turnstile != null) {
                turnstileDTO = turnstile.toDTO()
                turnstileDTO
            } else
                 null
            //turnstileRepository.getTurnstileById(turnstileId).toDTO()
        }
    }

    override suspend fun getTurnstileValidationByTicketId(ticketId: Long): TurnstileValidationDTO? {
        return withContext(Dispatchers.IO) {
            turnstileValidationRepository.getTurnstileValidationByTicketId(ticketId)
        }.firstOrNull()?.toDTO()
    }

    override suspend fun validateTicket(ticketQrDTO: TicketQrDTO, loggedTurnstileId: Long, authorizationHeader: String): Boolean {
        val ticketJwt: String? = ticketQrDTO.decodeQRCode()
        println("TEST $ticketJwt")
        val jwtUtils = JwtUtils(jwtTicketsSecretB64Key)
        val validation = jwtUtils.validateJwt(ticketJwt)
        if(!validation)
            return false
        else {
            val ticketDTO: TicketDTO = jwtUtils.getDetailsJwtTicket(ticketJwt)
            try {
                val flag = setTicketAsUsed(ticketDTO.sub, authorizationHeader)
                if(!flag)
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
            }catch(ex: Exception){
                return false
            }
        }
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

    private suspend fun setTicketAsUsed(ticketId: Long, authorizationHeader: String): Boolean {
        val flag: Boolean = travelerWebClient
            .put()
            .uri("embedded/${ticketId}")
            .accept(MediaType.APPLICATION_JSON)
            .header(jwtHttpHeaderName, authorizationHeader)
            .retrieve()
            .onStatus({ !it.equals(HttpStatus.OK) }) { resp ->
                resp.bodyToMono(String::class.java).map { Exception(it) }
            }
            .awaitBody()
        return flag
    }
}
