package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.turnstileservice.entities.Turnstile

data class TurnstileDTO (
    @JsonProperty val turnstileId: Long,
    @JsonProperty val zid: String,
)

fun Turnstile.toDTO(): TurnstileDTO {
    return TurnstileDTO(this.turnstileId, this.zid)
}