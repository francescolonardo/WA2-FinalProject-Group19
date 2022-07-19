package it.polito.wa2.login_service.repositories

import it.polito.wa2.login_service.entities.Activation
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Repository
interface ActivationRepository : CrudRepository<Activation, Long> {
    @Transactional(readOnly = true)
    //@Lock(LockModeType.READ) // TODO: check this
    @Query("select a from Activation a where a.provisionalId = :provisionalId")
    fun getActivationByProvisionalId(provisionalId: UUID): Activation?

    @Transactional(readOnly = true)
    //@Lock(LockModeType.READ) // TODO: check this
    @Query("select a from Activation a where a.deadline < :date")
    fun getExpiredActivations(date: LocalDate): List<Activation>?

    @Transactional
    @Modifying
    @Query("update Activation a set a.attemptCounter=a.attemptCounter-1 where a.provisionalId = :provisionalId")
    fun decrementAttemptCounterByProvisionalId(provisionalId: UUID)

    @Transactional
    @Modifying
    @Query("delete from Activation a where a.provisionalId = :provisionalId")
    fun deleteActivationByProvisionalId(provisionalId: UUID)

}
