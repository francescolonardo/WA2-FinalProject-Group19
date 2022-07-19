package it.polito.wa2.payment_service.security

import it.polito.wa2.payment_service.dtos.UserDetailsDTO
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtAuthFilter(
    base64Key: String,
    private val jwtHttpHeaderName: String,
    private val jwtHttpHeaderPrefix: String
) : WebFilter {
    private val jwtUtils = JwtUtils(base64Key)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authorizationHeader = exchange.request.headers[jwtHttpHeaderName]
        if (authorizationHeader.isNullOrEmpty() || !authorizationHeader[0].startsWith(jwtHttpHeaderPrefix)) {
            return chain.filter(exchange)
        }

        val authorizationToken = authorizationHeader[0].split(" ")[1]
        if (!jwtUtils.validateJwt(authorizationToken)) {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            return chain.filter(exchange)
        }

        val userDetailsDTO: UserDetailsDTO = jwtUtils.getDetailsJwt(authorizationToken)
        var authorities: Collection<SimpleGrantedAuthority> = ArrayList()
        userDetailsDTO.roles.forEach { role ->
            authorities += SimpleGrantedAuthority(role.toString())
        }
        val authentication = UsernamePasswordAuthenticationToken(userDetailsDTO.username, null, authorities)

        return chain.filter(exchange).contextWrite {
            ReactiveSecurityContextHolder.withAuthentication(authentication)
        }
    }
}
