package it.polito.wa2.traveler_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.traveler_service.entities.TicketPurchased
import java.sql.Timestamp

data class TicketPurchasedDTO (
    @JsonProperty val sub: Long = 0L,
    @JsonProperty val iat: Timestamp? = null,
    @JsonProperty val exp: Timestamp? = null,
    @JsonProperty val zid: String = "",
    @JsonProperty val jws: String = ""
)

fun TicketPurchased.toDTO(): TicketPurchasedDTO {
    return TicketPurchasedDTO(sub, iat, exp, zid, jws)
}
