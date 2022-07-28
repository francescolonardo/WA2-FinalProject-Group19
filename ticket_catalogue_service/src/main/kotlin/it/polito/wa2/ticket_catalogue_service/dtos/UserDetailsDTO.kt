package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetailsDTO (
    @JsonProperty("username") val username: String,
    @JsonProperty("roles") val roles: Set<Role>,
    @JsonProperty("date_of_birth") val dateOfBirth: String?,
    @JsonProperty("address") val address: String?,
    @JsonProperty("telephone_number") val telephoneNumber: String?
)

enum class Role {
    CUSTOMER,
    ADMIN
}
