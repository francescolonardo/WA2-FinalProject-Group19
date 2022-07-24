package it.polito.wa2.login_service.entities

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Activation(
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    var provisionalId: UUID = UUID.randomUUID(),
    var activationCode: String = "",
    var deadline: LocalDate = LocalDate.now().plusDays(3),
    var attemptCounter: Int = 5,
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne
    var user: User? = null
)
