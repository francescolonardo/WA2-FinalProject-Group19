package it.polito.wa2.turnstileservice.dtos

class JwtSubjectInfoDTO(
    val usernameId: String = "",
    val roles: Set<Role> = mutableSetOf()
)

enum class Role {
    ADMIN,
    CUSTOMER,
    EMBEDDED
}
