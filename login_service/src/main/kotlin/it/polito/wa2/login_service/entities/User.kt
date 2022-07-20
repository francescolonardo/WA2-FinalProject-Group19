package it.polito.wa2.login_service.entities

import javax.persistence.*

@Entity
@Table(name = "\"user\"")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
    @Column(unique = true)
    var username: String = ""
    var password: String = ""
    @Column(unique = true)
    var email: String = ""
    var active: Int = 0
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER) // loads all the roles whenever load a user
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = mutableSetOf(Role.CUSTOMER)
}
