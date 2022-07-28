package it.polito.wa2.turnstileservice.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.turnstileservice.dtos.*
import java.security.Key
import java.sql.Timestamp
import java.util.*

class JwtUtils(base64Key: String) {
    private val signatureKey: Key

    init {
        val byteKey = Base64.getDecoder().decode(base64Key)
        signatureKey = Keys.hmacShaKeyFor(byteKey)
    }

    fun validateJwt(token: String?): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signatureKey)
                .build()
                .parseClaimsJws(token)
        } catch (ex: Throwable) {
            println(ex.localizedMessage)
            return false
        }
        return true
    }

    data class UserRoles(val id : Long, val roles : Set<Role>)

    fun getDetailsJwt(authToken: String?): UserRoles {
        val decodedJwt = Jwts.parserBuilder()
            .setSigningKey(signatureKey)
            .build()
            .parseClaimsJws(authToken)

        val id = decodedJwt.body["sub"] as String
        val rolesString: List<String> = decodedJwt.body["roles"] as List<String>
        val roles: MutableSet<Role> = mutableSetOf()
        rolesString.map { roleString ->
            roles += when (roleString) {
                "ADMIN" -> Role.ADMIN
                else -> Role.EMBEDDED
            }
        }
        return UserRoles(id.toLong(), roles)
    }

    fun getDetailsJwtTicket(authToken: String?): TicketDTO {
        val decodedJwt = Jwts.parserBuilder()
            .setSigningKey(signatureKey)
            .build()
            .parseClaimsJws(authToken)
        val sub = decodedJwt.body["sub"] as String
        val iat = decodedJwt.body["iat"] as Int
        val exp = decodedJwt.body["exp"] as Int
        val zid = decodedJwt.body["zid"] as String
        val used = decodedJwt.body["used"] as Boolean // TODO: remove this
        val username = decodedJwt.body["username"] as String
        return TicketDTO(
            sub.toLong(),
            Timestamp(iat.toLong()),
            Timestamp(exp.toLong()),
            zid,
            used,
            username
        )
    }
}
