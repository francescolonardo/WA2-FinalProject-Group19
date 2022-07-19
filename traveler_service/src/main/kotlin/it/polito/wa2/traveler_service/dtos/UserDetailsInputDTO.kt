package it.polito.wa2.traveler_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDetailsInputDTO (
    @JsonProperty("address")
    var address: String = "",
    @JsonProperty("telephone_number")
    var telephoneNumber: String = ""
)
