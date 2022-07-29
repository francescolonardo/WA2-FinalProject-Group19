package it.polito.wa2.ticket_catalogue_service.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("tickets")
data class Ticket(
    @Id
    val id: Long?,
    @Column("type_")
    val type: TicketType,
    val validityZones: String,
    val price: Float,
    val minAge: Int?,
    val maxAge: Int?
)

enum class TicketType {
    ORDINAL,
    WEEKEND
}
