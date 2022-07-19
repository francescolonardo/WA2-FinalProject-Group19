package it.polito.wa2.traveler_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class BuyTicketsRequestDTO (
    @JsonProperty val cmd: String,
    @JsonProperty val quantity: Int,
    @JsonProperty val zones: String
)
