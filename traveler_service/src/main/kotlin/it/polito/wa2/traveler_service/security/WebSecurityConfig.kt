package it.polito.wa2.traveler_service.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String
    @Value("\${jwt.authorization.http-header-prefix}")
    private lateinit var jwtHttpHeaderPrefix: String

    override fun configure(http: HttpSecurity) {
        val jwtAuthFilter = JwtAuthFilter(jwtSecretB64Key, jwtHttpHeaderName, jwtHttpHeaderPrefix)

        http.csrf().disable() // disable cross site request forgery
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // to use JWT instead of the default stateful session policy (cookie)

        // authorization's granular rules
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET,"/my/profile").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.PUT,"/my/profile").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.GET,"/my/tickets").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.POST,"/my/tickets").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.GET,"/admin/travelers").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.GET,"/admin/traveler/**/profile").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.GET,"/admin/traveler/**/tickets").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.PUT,"/embedded/**").hasAuthority("EMBEDDED")

        http.authorizeRequests()
            .anyRequest().authenticated() // allows only authenticated users to be able to access the remaining paths

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
