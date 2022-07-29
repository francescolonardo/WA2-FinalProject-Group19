package it.polito.wa2.turnstileservice.security

import it.polito.wa2.turnstileservice.dtos.JwtSubjectInfoDTO
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

        val jwtSubjectInfoDTO: JwtSubjectInfoDTO = jwtUtils.getDetailsJwt(authorizationToken)
        var authorities: Collection<SimpleGrantedAuthority> = ArrayList()
        jwtSubjectInfoDTO.roles.forEach { role ->
            authorities += SimpleGrantedAuthority(role.toString())
        }
        val authentication = UsernamePasswordAuthenticationToken(jwtSubjectInfoDTO.usernameId, null, authorities)

        return chain.filter(exchange).contextWrite {
            ReactiveSecurityContextHolder.withAuthentication(authentication)
        }
    }
}
