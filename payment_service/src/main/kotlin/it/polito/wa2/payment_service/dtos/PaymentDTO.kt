package it.polito.wa2.payment_service.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentDTO (
    @JsonProperty("total_cost") val totalCost: Float,
    @JsonProperty("card_holder") val cardHolder: String,
    @JsonProperty("card_number") val cardNumber: String,
    @JsonProperty("card_exp_date") val cardExpDate: String,
    @JsonProperty("card_cvv") val cardCvv: String,
    @JsonProperty("order_id") val orderId: Long,
    @JsonProperty("username") val username: String
)
