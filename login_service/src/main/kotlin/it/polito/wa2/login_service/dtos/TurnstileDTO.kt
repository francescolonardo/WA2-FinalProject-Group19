package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class TurnstileDTO (
    @JsonProperty val id: Long,
    @JsonProperty val secret: String
)