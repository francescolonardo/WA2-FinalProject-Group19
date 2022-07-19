package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class BuyTicketsRequestDTO (
    @JsonProperty val cmd: String,
    @JsonProperty val quantity: Int,
    @JsonProperty val zones: String
)
