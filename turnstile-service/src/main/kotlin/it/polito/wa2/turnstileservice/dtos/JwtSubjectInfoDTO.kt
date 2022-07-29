package it.polito.wa2.turnstileservice.dtos

class JwtSubjectInfoDTO(
    val id: Long = 0L,
    val roles: Set<Role> = mutableSetOf()
)

enum class Role {
    ADMIN,
    CUSTOMER,
    EMBEDDED
}
