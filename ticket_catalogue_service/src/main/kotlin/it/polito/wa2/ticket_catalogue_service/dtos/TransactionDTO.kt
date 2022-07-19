package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.ticket_catalogue_service.entities.OrderStatus

data class TransactionDTO (
    @JsonProperty("order_id") val orderId: Long,
    @JsonProperty("order_status") val orderStatus: OrderStatus,
    @JsonProperty("username") val username: String,
    @JsonProperty("total_cost") val totalCost: Float
)
