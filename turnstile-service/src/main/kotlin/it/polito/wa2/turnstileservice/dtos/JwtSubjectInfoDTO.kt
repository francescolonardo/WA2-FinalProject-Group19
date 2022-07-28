package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

class JwtSubjectInfoDTO(
    @JsonProperty val id: Long,
    @JsonProperty val roles: Set<Role> = mutableSetOf()
)

enum class Role {
    ADMIN,
    CUSTOMER,
    EMBEDDED
}
