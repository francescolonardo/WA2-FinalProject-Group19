package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ActivationOutputDTO (
    @JsonProperty("provisional_id") val provisionalId: UUID,
    @JsonProperty("email") val email: String
)
