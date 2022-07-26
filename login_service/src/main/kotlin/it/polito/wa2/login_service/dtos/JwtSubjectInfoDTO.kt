package it.polito.wa2.login_service.dtos

import it.polito.wa2.login_service.entities.Role

data class JwtSubjectInfoDTO(
    val username: String = "",
    val roles: Set<Role> = mutableSetOf()
)
