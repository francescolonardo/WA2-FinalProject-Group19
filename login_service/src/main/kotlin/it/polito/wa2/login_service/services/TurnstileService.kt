package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.TurnstileDTO
import it.polito.wa2.login_service.dtos.TurnstileOutputDTO

interface TurnstileService {
    fun registerTurnstile(secret: String) : TurnstileOutputDTO

    fun generateToken(turnstileDTO: TurnstileDTO) : String
}