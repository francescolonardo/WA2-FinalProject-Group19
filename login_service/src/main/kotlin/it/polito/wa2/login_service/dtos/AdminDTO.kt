package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.login_service.entities.Role
import it.polito.wa2.login_service.entities.User

data class AdminDTO(
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("enrolling_capability") val enrollingCapability: Int = 0,
    @JsonProperty("roles") val roles: Set<Role> = mutableSetOf(Role.ADMIN)
)

fun User.toAdminDTO(): AdminDTO {
    return AdminDTO(id, username, password)
}
