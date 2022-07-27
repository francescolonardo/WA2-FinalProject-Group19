package it.polito.wa2.ticket_catalogue_service.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp
@Table("orders")
data class Order (
    @Id
    val id: Long?,
    val ticketId: Long,
    val quantity: Int,
    var status: OrderStatus,
    val username: String,
    var purchased: Boolean,
    var orderdate: Timestamp
)

enum class OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}
