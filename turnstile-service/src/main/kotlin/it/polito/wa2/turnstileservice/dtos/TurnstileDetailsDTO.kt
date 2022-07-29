package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.turnstileservice.entities.TurnstileDetails

data class TurnstileDetailsDTO(
    @JsonProperty("turnstile_id") val turnstileId: Long,
    @JsonProperty("zid") val zid: String,
)

fun TurnstileDetails.toDTO(): TurnstileDetailsDTO {
    return TurnstileDetailsDTO(id, zid)
}
