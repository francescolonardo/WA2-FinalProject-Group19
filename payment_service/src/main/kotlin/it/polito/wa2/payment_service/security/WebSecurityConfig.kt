package it.polito.wa2.payment_service.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class WebSecurityConfig {
    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var base64Key: String
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String
    @Value("\${jwt.authorization.http-header-prefix}")
    private lateinit var jwtHttpHeaderPrefix: String

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        val jwtAuthFilter = JwtAuthFilter(base64Key, jwtHttpHeaderName, jwtHttpHeaderPrefix)

        http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.GET,"/transactions").hasAuthority("CUSTOMER")
            .pathMatchers(HttpMethod.GET,"/admin/transactions").hasAuthority("ADMIN")
            .anyExchange().authenticated()

        http.addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        return http.build()
    }
}
