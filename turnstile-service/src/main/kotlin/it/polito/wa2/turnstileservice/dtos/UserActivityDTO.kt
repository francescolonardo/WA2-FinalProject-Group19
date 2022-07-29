package it.polito.wa2.turnstileservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

class UserActivityDTO {
    @JsonProperty var username: String = ""
    @JsonProperty var count: Long = 0L
}
