package it.polito.wa2.login_service.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Turnstile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0L
    var secret : String = ""
}