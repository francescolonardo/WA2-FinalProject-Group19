package it.polito.wa2.turnstileservice.services

import it.polito.wa2.turnstileservice.dtos.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TurnstileService {
    suspend fun validateTicket(ticketQrDTO: TicketQrDTO, loggedTurnstileId: Long, authorizationHeader: String): Boolean
    suspend fun getTurnstileDetails(turnstileId: Long): TurnstileDetailsDTO?
    suspend fun addTurnstileDetails(turnstileDetailsDTO: TurnstileDetailsDTO): TurnstileDetailsDTO
    suspend fun getTurnstileValidationByTicketId(ticketId: Long): TurnstileValidationDTO?
    suspend fun getTurnstileTransitCount(turnstileId: Long): TurnstileActivityDTO
    suspend fun getAllTurnstilesTransitCount(): TurnstileActivityDTO
    suspend fun getTurnstileTransitCountPeriod(turnstileId: Long, startPeriod: LocalDateTime, endPeriod: LocalDateTime): TurnstileActivityDTO
    suspend fun getAllTurnstilesTransitCountPeriod(startPeriod: LocalDateTime, endPeriod: LocalDateTime): TurnstileActivityDTO
    suspend fun getUserTransitCount(username: String): UserActivityDTO
    suspend fun getUserTransitCountPeriod(username: String, startPeriod: LocalDateTime, endPeriod: LocalDateTime): UserActivityDTO
    suspend fun getAllUserTransits(username: String): Flow<TurnstileValidationDTO?>
}
