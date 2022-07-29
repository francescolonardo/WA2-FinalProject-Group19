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

    fun getDetailsJwt(authToken: String?): JwtSubjectInfoDTO {
        val decodedJwt = Jwts.parserBuilder()
            .setSigningKey(signatureKey)
            .build()
            .parseClaimsJws(authToken)
        val usernameId = decodedJwt.body["sub"] as String
        val rolesString: List<String> = decodedJwt.body["roles"] as List<String>
        val roles: MutableSet<Role> = mutableSetOf()
        rolesString.map { roleString ->
            roles += when (roleString) {
                "CUSTOMER" -> Role.CUSTOMER
                "EMBEDDED" -> Role.EMBEDDED
                else -> Role.ADMIN
            }
        }
        return JwtSubjectInfoDTO(usernameId, roles)
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
        val username = decodedJwt.body["username"] as String
        return TicketDTO(
            sub.toLong(),
            Timestamp(iat.toLong()),
            Timestamp(exp.toLong()),
            zid,
            username
        )
    }
}
