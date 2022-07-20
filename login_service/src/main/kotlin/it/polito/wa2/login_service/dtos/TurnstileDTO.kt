package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class TurnstileDTO (
    @JsonProperty val secret: String
)