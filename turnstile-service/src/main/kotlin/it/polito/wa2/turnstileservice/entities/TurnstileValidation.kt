package it.polito.wa2.turnstileservice.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("turnstile_validation")
class TurnstileValidation {
    @Id
    var id: Long = 0L
    var turnstileId: Long = 0L
    var ticketId: Long = 0L
    var username: String = ""
    var dateTime: LocalDateTime? = null
}
