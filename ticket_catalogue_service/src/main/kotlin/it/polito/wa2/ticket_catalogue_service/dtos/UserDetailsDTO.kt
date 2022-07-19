package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class UserDetailsDTO (
    @JsonProperty("username") val username: String,
    @JsonProperty("roles") val roles: Set<Role>,
    @JsonProperty("dateOfBirth") val dateOfBirth: Date?,
    @JsonProperty("address") val address: String?,
    @JsonProperty("telephoneNumber") val telephoneNumber: String?
)

enum class Role {
    CUSTOMER,
    ADMIN
}
