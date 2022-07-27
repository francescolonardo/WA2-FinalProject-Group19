package it.polito.wa2.login_service.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import it.polito.wa2.login_service.dtos.JwtSubjectInfoDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.HashMap
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthFilter(
    base64Key: String,
    private val jwtHttpHeaderName: String,
    private val jwtHttpHeaderPrefix: String
    ) : OncePerRequestFilter() {
    private val jwtUtils = JwtUtils(base64Key)
    private lateinit var errorExplanation: String

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(jwtHttpHeaderName)
        if (authorizationHeader.isNullOrEmpty() ||
            !authorizationHeader.startsWith(jwtHttpHeaderPrefix)) {
            filterChain.doFilter(request, response)
            return
        }

        val authorizationToken = authorizationHeader.split(" ")[1]
        try {
            jwtUtils.validateJwt(authorizationToken)

            val jwtSubjectInfoDTO: JwtSubjectInfoDTO = jwtUtils.getDetailsJwt(authorizationToken)
            var authorities: Collection<SimpleGrantedAuthority> = ArrayList()
            jwtSubjectInfoDTO.roles.forEach { role ->
                authorities += SimpleGrantedAuthority(role.toString())
            }
            val authentication = UsernamePasswordAuthenticationToken(jwtSubjectInfoDTO.username, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response) // lets the request continuing its course
        } catch (ex: Exception) {
            errorExplanation = when (ex) {
                is ExpiredJwtException -> {
                    "expired JWT"
                }
                else -> {
                    "malformed JWT"
                }
            }
            response.resetBuffer()
            response.status = HttpStatus.FORBIDDEN.value()
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            val error = HashMap<String, String>()
            error["error"] = errorExplanation
            response.outputStream.print(ObjectMapper().writeValueAsString(error))
            response.flushBuffer()
        }
    }
}
