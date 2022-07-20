package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.TurnstileOutputDTO

interface TurnstileService {
    fun registerTurnstile(secret: String) : TurnstileOutputDTO
}