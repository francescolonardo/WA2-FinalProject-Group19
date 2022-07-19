package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class TicketPurchasedDTO (
    @JsonProperty val sub: Long = 0L,
    @JsonProperty val iat: Timestamp? = null,
    @JsonProperty val exp: Timestamp? = null,
    @JsonProperty val zid: String = "",
    @JsonProperty val jws: String = ""
)
