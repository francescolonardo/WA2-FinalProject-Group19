package it.polito.wa2.login_service.services

import it.polito.wa2.login_service.dtos.*
import java.util.UUID

interface UserService {
    fun registerTraveler(username: String, password: String, email: String): ActivationOutputDTO
    fun validateTraveler(provisionalId: UUID, activationCode: String): TravelerOutputDTO
    fun loginUser(username: String, password: String): AuthorizationTokenDTO
    fun changePasswordUser(username: String, oldPassword: String, newPassword: String)
    fun deleteAccountTraveler(username: String)
    fun enrollAdmin(loggedUsername: String, newAdminUsername: String, newAdminPassword: String, newAdminEnrollingCapability: Int): AdminOutputDTO
    fun enrollDefaultAdmin(newAdminUsername: String, newAdminPassword: String, newAdminEnrollingCapability: Int)
    fun disableAccountAdmin(username: String, userId: Long)
}
