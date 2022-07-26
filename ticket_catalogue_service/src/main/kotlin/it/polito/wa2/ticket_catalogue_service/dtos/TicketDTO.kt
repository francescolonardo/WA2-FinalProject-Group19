package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.ticket_catalogue_service.entities.Ticket
import it.polito.wa2.ticket_catalogue_service.entities.TicketType

class TicketDTO(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("type") val type: TicketType,
    @JsonProperty("validity_zones") val validityZones: String,
    @JsonProperty("price") val price: Float,
    @JsonProperty("min_age") val minAge: Int?,
    @JsonProperty("max_age") val maxAge: Int?
)

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(this.id, this.type, this.validityZones, this.price, this.minAge, this.maxAge)
}
