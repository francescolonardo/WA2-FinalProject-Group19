package it.polito.wa2.payment_service.entities

import it.polito.wa2.payment_service.dtos.OrderStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Transaction(
    @Id
    val id: Long?,
    val orderId: Long,
    val orderStatus: OrderStatus,
    val username: String,
    val totalCost: Float
)
