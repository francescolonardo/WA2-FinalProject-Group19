package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.login_service.entities.Activation
import it.polito.wa2.login_service.entities.User
import java.time.LocalDate
import java.util.*

data class ActivationDTO (
    @JsonProperty("provisional_id") val provisionalId: UUID,
    @JsonProperty("activation_code") val activationCode: String,
    @JsonProperty("deadline") val deadline: LocalDate?,
    @JsonProperty("attempt_counter") val attemptCounter: Int = 5,
    @JsonProperty("user") val user: User?
)

fun Activation.toDTO(): ActivationDTO {
    return ActivationDTO(provisionalId, activationCode, deadline, attemptCounter, user)
}
