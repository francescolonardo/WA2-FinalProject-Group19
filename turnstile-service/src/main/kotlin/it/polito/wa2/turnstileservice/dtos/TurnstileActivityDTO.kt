package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

class TurnstileActivityDTO {
    @JsonProperty
    var turnstileId: Long? = 0L
    @JsonProperty
    var count: Long? = 0L
}