package it.polito.wa2.login_service.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.login_service.entities.Role
import java.security.Key
import java.util.*

class JwtUtils(base64Key: String) {
    private val signatureKey: Key

    init {
        val byteKey = Base64.getDecoder().decode(base64Key)
        signatureKey = Keys.hmacShaKeyFor(byteKey)
    }

    fun validateJwt(authToken: String): Boolean {
        try {
            val decodedJwt = Jwts.parserBuilder()
                .setSigningKey(signatureKey)
                .build()
                .parseClaimsJws(authToken)
        } catch (ex: Throwable) {
            return false
        }
        return true
    }

    fun getDetailsJwt(authToken: String): UserRoles {
        val decodedJwt = Jwts.parserBuilder()
            .setSigningKey(signatureKey)
            .build()
            .parseClaimsJws(authToken)

        val username = decodedJwt.body["sub"] as String
        val rolesString: List<String> = decodedJwt.body["roles"] as List<String>
        val roles: MutableSet<Role> = mutableSetOf()
        rolesString.map { roleString ->
            roles += when (roleString) {
                "ADMIN" -> Role.ADMIN
                "EMBEDDED" -> Role.EMBEDDED
                else -> Role.CUSTOMER
            }
        }
        return UserRoles(username, roles)
    }
}

data class UserRoles(val username : String, val roles : Set<Role>)
