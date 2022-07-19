package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.entities.User
import it.polito.wa2.login_service.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional // TODO: check if we really need it
class LoginServiceImpl: UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.getUserByUsername(username)
            ?: throw UsernameNotFoundException("User not found in the database")
        var authorities: Collection<SimpleGrantedAuthority> = ArrayList()
        user.roles.forEach { role ->
            authorities += SimpleGrantedAuthority(role.name)
        }
        return org.springframework.security.core.userdetails.User(user.username, user.password, authorities)
    }

}
