package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.ActivationOutputDTO
import it.polito.wa2.login_service.dtos.AuthorizationTokenDTO
import it.polito.wa2.login_service.dtos.UserOutputDTO
import java.util.UUID

interface UserService {
    fun registerUser(username: String, password: String, email: String): ActivationOutputDTO
    fun validateUser(provisionalId: UUID, activationCode: String): UserOutputDTO
    fun loginUser(username: String, password: String): AuthorizationTokenDTO
}
