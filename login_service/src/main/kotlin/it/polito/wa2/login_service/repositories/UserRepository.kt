package it.polito.wa2.login_service.repositories

import it.polito.wa2.login_service.entities.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : CrudRepository<User, Long> {
    @Transactional(readOnly = true)
    //@Lock(LockModeType.READ) // TODO: check this
    @Query("select u from User u where u.username = :username or u.email = :email")
    fun getUserByUsernameOrEmail(username: String, email: String): User?

    @Transactional(readOnly = true)
    //@Lock(LockModeType.READ) // TODO: check this
    @Query("select u from User u where u.username = :username")
    fun getUserByUsername(username: String): User?

    @Transactional
    @Modifying
    @Query("update User u set u.active = 1 where u.id = :id")
    fun activateById(id: Long)
    @Transactional
    @Modifying
    @Query("update User u set u.active = 0 where u.id = :id")
    fun deactivateById(id: Long)

    @Transactional(readOnly = true)
    fun findByUsername(username: String): User?

    fun deleteByUsername(username: String): Int
}
