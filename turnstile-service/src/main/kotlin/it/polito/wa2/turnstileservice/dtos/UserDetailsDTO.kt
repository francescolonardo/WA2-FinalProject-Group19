package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

class UserDetailsDTO(
    @JsonProperty val id: Long,
    @JsonProperty val role: Role,
)

enum class Role{
    ADMIN,
    EMBEDDED
}