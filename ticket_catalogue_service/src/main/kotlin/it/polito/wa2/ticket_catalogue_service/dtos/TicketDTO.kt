package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.ticket_catalogue_service.entities.Ticket
import it.polito.wa2.ticket_catalogue_service.entities.TicketType

class TicketDTO (
    @JsonProperty("id") val id: Long?,
    @JsonProperty("type") val type: TicketType,
    @JsonProperty("price") val price: Float,
    @JsonProperty("min_age") val minAge: Int?,
    @JsonProperty("max_age") val maxAge: Int?
    @JsonProperty("used") val used : Boolean
)

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(this.id, this.type, this.price, this.minAge, this.maxAge, this.used)
}
