package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.*
import java.util.UUID

interface UserService {
    fun registerTraveler(username: String, password: String, email: String): ActivationOutputDTO
    fun validateTraveler(provisionalId: UUID, activationCode: String): TravelerOutputDTO
    fun enrollAdmin(username: String, password: String, enrollingCapability: Int): AdminOutputDTO
    fun loginUser(username: String, password: String): AuthorizationTokenDTO
}
