package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.login_service.entities.Role
import it.polito.wa2.login_service.entities.User

data class UserDTO (
    @JsonProperty("id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("active") val active: Int = 0,
    @JsonProperty("roles") val roles: Set<Role> = mutableSetOf() // TODO: check if we really need it
)

fun User.toDTO(): UserDTO {
    return UserDTO(id, username, password, email, active, roles)
}
