package it.polito.wa2.turnstileservice.dtos

class JwtSubjectInfoDTO(
    val id: Long? = null,
    val roles: Set<Role> = mutableSetOf()
)

enum class Role {
    ADMIN,
    CUSTOMER,
    EMBEDDED
}
