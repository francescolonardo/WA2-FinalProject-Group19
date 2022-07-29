package it.polito.wa2.turnstileservice.repositories

import it.polito.wa2.turnstileservice.entities.TurnstileDetails
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TurnstileRepository : CoroutineCrudRepository<TurnstileDetails, Long> {
    suspend fun findByTurnstileId(turnstileId: Long): TurnstileDetails?
}
