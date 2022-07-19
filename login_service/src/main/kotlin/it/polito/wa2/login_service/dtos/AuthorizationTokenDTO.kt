package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthorizationTokenDTO (
    @JsonProperty("Authorization") val authorizationToken: String
)
