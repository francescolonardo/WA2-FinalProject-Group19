package it.polito.wa2.turnstileservice.repositories

import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface TurnstileValidationRepository: CoroutineCrudRepository<TurnstileValidation, Long>{

    @Transactional(readOnly = true)
    @Query("""
     SELECT * FROM turnstile_validation
     WHERE ticket_sub = :ticketId
    """)
    fun getTurnstileValidationByTicketId(ticketId: Long): Flow<TurnstileValidation?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
     WHERE turnstile_id = :turnstileId
     GROUP BY turnstile_id
    """)
    fun getTurnstileTransitCount(turnstileId: Long): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
    """)
    fun getAllTurnstilesTransitCount(): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
     WHERE turnstile_id = :turnstileId AND date_time >= :startPeriod AND date_time <= :endPeriod
     GROUP BY turnstile_id
    """)
    fun getTurnstileTransitCountPeriod(turnstileId: Long, startPeriod: LocalDateTime, endPeriod: LocalDateTime): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
     WHERE date_time >= :startPeriod AND date_time <= :endPeriod
    """)
    fun getAllTurnstilesTransitCountPeriod(startPeriod: LocalDateTime, endPeriod: LocalDateTime): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
     WHERE username = :username
    """)
    fun getUserTransitCount(username: String): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT count(*) FROM turnstile_validation
     WHERE username = :username AND date_time >= :startPeriod AND date_time <= :endPeriod
    """)
    fun getUserTransitCountPeriod(username: String, startPeriod: LocalDateTime, endPeriod: LocalDateTime): Flow<Long?>

    @Transactional(readOnly = true)
    @Query("""
     SELECT * FROM turnstile_validation
     WHERE username = :username 
    """)
    fun getAllUserTransits(username: String): Flow<TurnstileValidation?>






}