package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.ticket_catalogue_service.entities.Order
import it.polito.wa2.ticket_catalogue_service.entities.OrderStatus

data class OrderDTO (
    @JsonProperty("id") val id: Long?,
    @JsonProperty("ticket_id") val ticketId: Long,
    @JsonProperty("quantity") val quantity: Int,
    @JsonProperty("status") val status: OrderStatus,
    @JsonProperty("username") val username: String
)

fun Order.toDTO(): OrderDTO {
    return OrderDTO(this.id, this.ticketId, this.quantity, this.status, this.username)
}
