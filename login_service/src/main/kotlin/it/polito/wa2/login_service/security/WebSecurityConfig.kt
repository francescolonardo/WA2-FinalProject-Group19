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

@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var loginService: LoginServiceImpl
    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    @Value("\${jwt.authorization.signature-key-base64}")
    private lateinit var jwtSecretB64Key: String
    @Value("\${jwt.authorization.expiration-time-ms}")
    private lateinit var jwtExpirationTimeMs: String
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(loginService)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val userAuthenticationFilter = UserAuthenticationFilter(authenticationManagerBean(), jwtSecretB64Key, jwtExpirationTimeMs.toInt(), jwtHttpHeaderName)
        userAuthenticationFilter.setFilterProcessesUrl("/user/login") // changes login path (where to post username and password)

        http.csrf().disable() // disable cross site request forgery
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // to use JWT instead of the default stateful session policy (cookie)

        // authorization's granular rules
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,"/user/register").permitAll()
            .antMatchers(HttpMethod.POST,"/user/validate").permitAll()
            .antMatchers(HttpMethod.GET,"/user/validate/**").permitAll()
            .antMatchers(HttpMethod.POST,"/user/login").permitAll()

        http.authorizeRequests()
            .anyRequest().authenticated() // allows only authenticated users to be able to access the remaining paths

        // TODO: uncomment this if you want to use the form login provided by Spring,
        // that requires a x-www-form-urlencoded body
        //http.addFilter(userAuthenticationFilter) // authentication filter for checking the user when he tries to log in
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}
