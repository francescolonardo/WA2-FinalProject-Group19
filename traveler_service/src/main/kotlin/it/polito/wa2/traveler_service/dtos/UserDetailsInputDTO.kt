package it.polito.wa2.traveler_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetailsInputDTO(
    @JsonProperty("date_of_birth")
    var dateOfBirth: String = "",
    @JsonProperty("address")
    var address: String = "",
    @JsonProperty("telephone_number")
    var telephoneNumber: String = ""
)
