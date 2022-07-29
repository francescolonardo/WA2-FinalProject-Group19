package it.polito.wa2.turnstileservice.services

import it.polito.wa2.turnstileservice.dtos.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TurnstileService {
    suspend fun validateTicket(loggedTurnstileId: Long, ticketQRDTO: TicketQRDTO, authorizationHeader: String): Boolean
    suspend fun getTurnstileDetails(turnstileId: Long): TurnstileDetailsDTO?
    suspend fun addTurnstileDetails(turnstileDetailsDTO: TurnstileDetailsDTO): TurnstileDetailsDTO
    suspend fun getTurnstileValidationsByTurnstileId(turnstileId: Long): Flow<TurnstileValidationDTO>
    suspend fun getTurnstilesValidationByTicketId(ticketId: Long): TurnstileValidationDTO?
    suspend fun getAllTurnstilesActivity(): Long
    suspend fun getTurnstileActivity(turnstileId: Long): Long
    suspend fun getAllTurnstilesActivityPeriod(startDate: LocalDateTime, endDate: LocalDateTime): Long
    suspend fun getTurnstileActivityPeriod(turnstileId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Long
    suspend fun getUserActivity(username: String): Long
    suspend fun getUserActivityPeriod(username: String, startPeriod: LocalDateTime, endPeriod: LocalDateTime): Long
    fun getAllUserTransits(username: String): Flow<TurnstileValidationDTO?>
}
