package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import it.polito.wa2.login_service.entities.Role

data class AdminOutputDTO(
    @JsonProperty("user_id") val id: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("enrolling_capability") val enrollingCapability: Int,
    @JsonProperty("roles") val roles: Set<Role>
)
