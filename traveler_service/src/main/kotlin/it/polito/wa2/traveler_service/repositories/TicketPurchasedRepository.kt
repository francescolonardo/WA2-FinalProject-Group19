package it.polito.wa2.traveler_service.repositories

import it.polito.wa2.traveler_service.entities.TicketPurchased
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TicketPurchasedRepository : CrudRepository<TicketPurchased, Long> {
    @Transactional(readOnly = true)
    @Query("select coalesce(max(tP.sub)+1L, 1L) from TicketPurchased tP")
    fun getNextSub(): Long
}
