package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.login_service.entities.Role
import it.polito.wa2.login_service.entities.User

data class TravelerDTO(
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("active") val active: Int = 0,
    @JsonProperty("roles") val roles: Set<Role> = mutableSetOf(Role.CUSTOMER)
)

fun User.toUserDTO(): TravelerDTO {
    return TravelerDTO(id, username, password, email, active)
}
