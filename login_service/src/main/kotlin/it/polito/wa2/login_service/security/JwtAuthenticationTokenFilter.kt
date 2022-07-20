package it.polito.wa2.login_service.security

import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import java.util.ArrayList
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationTokenFilter(
    base64Key: String,
    private val jwtHttpHeaderName: String,
    private val jwtHttpHeaderPrefix: String) : OncePerRequestFilter() {

    private val jwtUtils = JwtUtils(base64Key)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(jwtHttpHeaderName)
        if (authorizationHeader.isNullOrEmpty() || !authorizationHeader.startsWith(jwtHttpHeaderPrefix)) {
            filterChain.doFilter(request, response)
            return
        }

        val authorizationToken = authorizationHeader.split(" ")[1]
        if (!jwtUtils.validateJwt(authorizationToken)) {
            response.status = HttpStatus.FORBIDDEN.value()
            filterChain.doFilter(request, response)
            return
        }

        val userRoles: UserRoles = jwtUtils.getDetailsJwt(authorizationToken)
        var authorities: Collection<SimpleGrantedAuthority> = ArrayList()
        userRoles.roles.forEach { role ->
            authorities += SimpleGrantedAuthority(role.toString())
        }
        val authentication = UsernamePasswordAuthenticationToken(userRoles.username, null, authorities)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response) // lets the request continues its course
    }
}
