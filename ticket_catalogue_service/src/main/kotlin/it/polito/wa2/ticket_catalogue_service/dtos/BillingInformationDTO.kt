package it.polito.wa2.ticket_catalogue_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class BillingInformationDTO (
    @JsonProperty("ticket_id") val ticketId: Long,
    @JsonProperty("tickets_quantity") val ticketsQuantity: Int,
    @JsonProperty("card_holder") val cardHolder: String,
    @JsonProperty("card_number") val cardNumber: String,
    @JsonProperty("card_exp_date") val cardExpDate: String,
    @JsonProperty("card_cvv") val cardCvv: String
)
