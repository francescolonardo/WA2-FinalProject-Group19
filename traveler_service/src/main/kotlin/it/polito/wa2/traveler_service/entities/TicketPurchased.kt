package it.polito.wa2.traveler_service.entities

import java.sql.Timestamp
import javax.persistence.*

@Entity
class TicketPurchased {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var sub: Long = 0L
    var iat: Timestamp? = null
    var exp: Timestamp? = null
    var zid: String = ""
    var jws: String = ""
    var used: Boolean = false
    @ManyToOne
    var userDetails: UserDetails? = null
}
