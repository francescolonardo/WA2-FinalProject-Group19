package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLoginDTO(
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String
)
