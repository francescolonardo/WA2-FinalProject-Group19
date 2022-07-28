package it.polito.wa2.turnstileservice.repositories

import it.polito.wa2.turnstileservice.entities.Turnstile
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TurnstileRepository: CoroutineCrudRepository<Turnstile, Long>{
    @Transactional(readOnly = true)
    @Query("""
     SELECT * FROM turnstile
     WHERE turnstile_id = :turnstileId
    """)
    fun getTurnstileById(turnstileId: Long): Flow<Turnstile>
}
