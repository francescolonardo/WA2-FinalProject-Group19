package it.polito.wa2.login_service.repositories

import it.polito.wa2.login_service.entities.Turnstile
import org.springframework.data.repository.CrudRepository

interface TurnstileRepository: CrudRepository<Turnstile, Long> {
}