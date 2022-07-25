package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.*
import java.util.UUID

interface UserService {
    fun registerTraveler(username: String, password: String, email: String): ActivationOutputDTO
    fun validateTraveler(provisionalId: UUID, activationCode: String): TravelerOutputDTO
    fun loginUser(username: String, password: String): AuthorizationTokenDTO
    fun changePasswordUser(username: String, oldPassword: String, newPassword: String)
    fun deleteAccountTraveler(username: String)
    fun enrollAdmin(username: String, password: String, enrollingCapability: Int): AdminOutputDTO
    fun disableAccountAdmin(username: String, userId: Long)
}
