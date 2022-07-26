package it.polito.wa2.ticket_catalogue_service.controllers

import it.polito.wa2.ticket_catalogue_service.dtos.BillingInformationDTO
import it.polito.wa2.ticket_catalogue_service.exceptions.TicketNotFoundException
import it.polito.wa2.ticket_catalogue_service.services.OrderServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.HashMap

@RestController
@RequestMapping("/shop")
class ShopController {
    @Autowired
    private lateinit var orderService: OrderServiceImpl

    @PostMapping("/{ticketId}")
    suspend fun postNewOrder(
        @PathVariable("ticketId") ticketId: Long,
        @RequestBody billingInformationDTO: BillingInformationDTO,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<Any> {
        if (ticketId != billingInformationDTO.ticketId)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        return try {
            val orderId = HashMap<String, Long>()
            orderId["orderId"] = orderService.addNewOrder(billingInformationDTO, loggedUser.name, authorizationHeader)
                ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
            return ResponseEntity.ok(orderId)
        } catch (ex: TicketNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }
}
