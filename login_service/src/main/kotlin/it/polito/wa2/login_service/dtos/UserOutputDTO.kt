package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserOutputDTO(
    @JsonProperty("user_id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
)
