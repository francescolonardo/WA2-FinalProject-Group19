package it.polito.wa2.payment_service.dtos

import it.polito.wa2.payment_service.entities.Role
import java.util.*

data class UserDetailsDTO (
    val username: String,
    val roles: Set<Role>,
    val dateOfBirth: Date?,
    val address: String?,
    val telephoneNumber: String?
)
