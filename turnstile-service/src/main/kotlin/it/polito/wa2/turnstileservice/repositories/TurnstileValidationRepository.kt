package it.polito.wa2.turnstileservice.repositories

import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TurnstileValidationRepository: CoroutineCrudRepository<TurnstileValidation, Long>{
    fun findByTurnstileId(turnstileId: Long): Flow<TurnstileValidation>
    suspend fun findByTicketId(ticketId: Long): TurnstileValidation?
    suspend fun countByTurnstileId(turnstileId: Long): Long
    suspend fun countByDateTimeBetween(startDate: LocalDateTime, endDate: LocalDateTime): Long
    suspend fun countByTurnstileIdAndDateTimeBetween(turnstileId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Long
    suspend fun countByUsername(username: String): Long
    suspend fun countByUsernameAndDateTimeBetween(username: String, startDate: LocalDateTime, endDate: LocalDateTime): Long
    fun findAllByUsername(username: String): Flow<TurnstileValidation?>
}
