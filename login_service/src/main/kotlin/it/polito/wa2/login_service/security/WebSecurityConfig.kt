package it.polito.wa2.login_service.security

import it.polito.wa2.login_service.services.LoginServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String
    @Value("\${jwt.authorization.http-header-prefix}")
    private lateinit var jwtHttpHeaderPrefix: String

    @Autowired
    private lateinit var loginService: LoginServiceImpl
    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(loginService)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val userAuthenticationFilter = UserAuthenticationFilter(authenticationManagerBean())
        userAuthenticationFilter.setFilterProcessesUrl("/user/login") // changes login path (where to post username and password)
        val jwtAuthFilter = JwtAuthFilter(jwtSecretB64Key, jwtHttpHeaderName, jwtHttpHeaderPrefix)

        http.csrf().disable() // disable cross site request forgery
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // to use JWT instead of the default stateful session policy (cookie)

        // authorization's granular rules
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,"/user/register").permitAll()
            .antMatchers(HttpMethod.POST,"/user/validate").permitAll()
            .antMatchers(HttpMethod.GET,"/user/validate/**").permitAll()
            .antMatchers(HttpMethod.POST,"/user/login").permitAll()
            .antMatchers(HttpMethod.POST,"/user/changePassword").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.DELETE,"/user/deleteAccount").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.POST,"/admin/enrolling").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.GET,"/admin/disableAccount/**").hasAuthority("ADMIN")
            .anyRequest().authenticated() // allows only authenticated users to be able to access the remaining paths

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
