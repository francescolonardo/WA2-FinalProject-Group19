package it.polito.wa2.login_service

import it.polito.wa2.login_service.services.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class LoginServiceApplication {
    @Autowired
    private lateinit var userService: UserServiceImpl

    @Value("\${admin.default.username}")
    private lateinit var adminDefaultUsername: String
    @Value("\${admin.default.password}")
    private lateinit var adminDefaultPassword: String
    @Value("\${admin.default.enrolling_capability}")
    private var adminDefaultEnrollingCapability: Int = 0

    @Bean
    fun run(): CommandLineRunner {
        return CommandLineRunner {
            userService.enrollDefaultAdmin(
                adminDefaultUsername,
                adminDefaultPassword,
                adminDefaultEnrollingCapability
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<LoginServiceApplication>(*args)
}
