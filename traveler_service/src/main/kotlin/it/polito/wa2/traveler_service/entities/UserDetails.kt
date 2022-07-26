package it.polito.wa2.traveler_service.entities

import javax.persistence.*

@Entity
class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
    @Column(unique = true)
    var username: String = ""
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER) // loads all the roles whenever load a user
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = mutableSetOf()
    var dateOfBirth: String = ""
    var address: String = ""
    var telephoneNumber: String = ""
    @OneToMany
    var tickets: MutableList<TicketPurchased> = mutableListOf()
}
