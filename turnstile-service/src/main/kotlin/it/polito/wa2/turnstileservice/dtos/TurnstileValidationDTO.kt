package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import java.time.LocalDateTime

data class TurnstileValidationDTO(
    @JsonProperty val id: Long = 0L,
    @JsonProperty val turnstileId: Long? = 0L,
    @JsonProperty val ticketSub: Long? = 0L,
    @JsonProperty val dateTime: LocalDateTime? = null
)

fun TurnstileValidation.toDTO(): TurnstileValidationDTO {
    return TurnstileValidationDTO(this.id, this.turnstileId, this.ticketSub, this.dateTime)
}