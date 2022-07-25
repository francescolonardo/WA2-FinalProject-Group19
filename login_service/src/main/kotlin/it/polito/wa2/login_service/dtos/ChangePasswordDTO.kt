package it.polito.wa2.login_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class ChangePasswordDTO(
    @JsonProperty("old_password") val oldPassword: String,
    @JsonProperty("new_password") val newPassword: String
)
