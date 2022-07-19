package it.polito.wa2.payment_service.dtos

import it.polito.wa2.payment_service.entities.Transaction
import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionDTO (
    @JsonProperty("order_id") val orderId: Long,
    @JsonProperty("order_status") val orderStatus: OrderStatus,
    @JsonProperty("username") val username: String,
    @JsonProperty("total_cost") val totalCost: Float,
)

fun Transaction.toDTO():TransactionDTO{
    return TransactionDTO(orderId, orderStatus, username, totalCost)
}

enum class OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}
