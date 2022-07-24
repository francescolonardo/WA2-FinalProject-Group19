package it.polito.wa2.traveler_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.traveler_service.entities.Role
import it.polito.wa2.traveler_service.entities.UserDetails
import java.util.*

data class UserDetailsDTO(
    @JsonProperty("username")
    val username: String = "",
    @JsonProperty("roles")
    val roles: Set<Role> = mutableSetOf(),
    @JsonProperty("date_of_birth")
    val dateOfBirth: Date? = null,
    @JsonProperty("address")
    var address: String = "",
    @JsonProperty("telephone_number")
    var telephoneNumber: String = ""
)

fun UserDetails.toDTO(): UserDetailsDTO {
    return UserDetailsDTO(username, roles, dateOfBirth, address, telephoneNumber)
}
