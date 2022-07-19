package it.polito.wa2.login_service.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.security.Key
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

class UserAuthenticationFilter : UsernamePasswordAuthenticationFilter {
    private val jwtSecretB64Key: String
    private val jwtExpirationTimeMs: Int
    private val jwtHttpHeaderName: String

    constructor(authenticationManager: AuthenticationManager, jwtSecretB64Key: String, jwtExpirationTimeMs: Int, jwtHttpHeaderName: String) {
        this.authenticationManager = authenticationManager
        this.jwtSecretB64Key = jwtSecretB64Key
        this.jwtExpirationTimeMs = jwtExpirationTimeMs
        this.jwtHttpHeaderName = jwtHttpHeaderName
    }

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val username: String = request.getParameter("username")
        val password: String = request.getParameter("password")
        val authentication = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(authentication)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val jwtSecretByteKey = Base64.getDecoder().decode(jwtSecretB64Key)
        val jwtSecretKey: Key = Keys.hmacShaKeyFor(jwtSecretByteKey)
        val user: User = authentication.principal as User // retrieved logged user
        val accessToken = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationTimeMs))
            .claim("roles", user.authorities.map { it.authority })
            .signWith(jwtSecretKey)
            .compact()
        response.setHeader(jwtHttpHeaderName, accessToken)

        val token: HashMap<String, String> = HashMap()
        token[jwtHttpHeaderName] = accessToken
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, token)
    }

}
