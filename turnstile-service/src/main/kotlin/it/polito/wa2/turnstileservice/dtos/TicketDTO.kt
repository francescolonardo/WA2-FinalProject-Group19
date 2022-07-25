package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class TicketDTO (
    @JsonProperty val sub: Long = 0L,
    @JsonProperty val iat: Timestamp? = null,
    @JsonProperty val exp: Timestamp? = null,
    @JsonProperty val zid: String = "",
    @JsonProperty val used: Boolean,
    @JsonProperty val username: String = ""
)
