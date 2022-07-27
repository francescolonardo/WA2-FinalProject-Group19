package it.polito.wa2.traveler_service

import it.polito.wa2.traveler_service.entities.Role
import it.polito.wa2.traveler_service.services.TravelerServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class TravelerServiceApplication {
	@Autowired
	private lateinit var travelerService: TravelerServiceImpl

	@Bean
	fun run(): CommandLineRunner {
		return CommandLineRunner {
			travelerService.addUserDetails(
				"john",
				"address 123",
				"31/12/1999",
				"0123456789",
				Role.CUSTOMER
			)
			travelerService.addUserDetails(
				"jimmy",
				"address 321",
				"01/01/2000",
				"9876543210",
				Role.CUSTOMER
			)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<TravelerServiceApplication>(*args)
}
