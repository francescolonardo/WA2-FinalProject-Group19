package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.turnstileservice.entities.TurnstileValidation
import java.time.LocalDateTime

data class TurnstileValidationDTO(
    @JsonProperty("id")  val id: Long = 0L,
    @JsonProperty("turnstile_id") val turnstileId: Long? = 0L,
    @JsonProperty("ticket_id") val ticketId: Long? = 0L,
    @JsonProperty("date_time") val dateTime: LocalDateTime? = null
)

fun TurnstileValidation.toDTO(): TurnstileValidationDTO {
    return TurnstileValidationDTO(this.id, this.turnstileId, this.ticketId, this.dateTime)
}
