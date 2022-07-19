package it.polito.wa2.traveler_service.repositories

import it.polito.wa2.traveler_service.entities.UserDetails
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserDetailsRepository : CrudRepository<UserDetails, Long> {
    fun findUserDetailsByUsername(username: String): UserDetails?
    fun findUserDetailsById(id: Long): UserDetails?

    @Transactional(readOnly = true)
    @Query("select u from UserDetails u")
    fun getAllUserDetails(): List<UserDetails>?
}
