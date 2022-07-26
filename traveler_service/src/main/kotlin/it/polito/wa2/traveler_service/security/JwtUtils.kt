package it.polito.wa2.traveler_service.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.traveler_service.dtos.JwtSubjectInfoDTO
import it.polito.wa2.traveler_service.entities.Role
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
            ex.printStackTrace() // TODO: remove this (?)
            return false
        }
        return true
    }

    fun getDetailsJwt(authToken: String): JwtSubjectInfoDTO {
        val decodedJwt = Jwts.parserBuilder()
            .setSigningKey(signatureKey)
            .build()
            .parseClaimsJws(authToken)

        val username = decodedJwt.body["sub"] as String
        val rolesString: List<String> = decodedJwt.body["roles"] as List<String>
        val roles: MutableSet<Role> = mutableSetOf()
        rolesString.map { roleString ->
            roles += when (roleString) {
                "CUSTOMER" -> Role.CUSTOMER
                "EMBEDDED" -> Role.EMBEDDED
                else -> Role.ADMIN
            }
        }
        return JwtSubjectInfoDTO(username, roles)
    }
}
