package it.polito.wa2.ticket_catalogue_service.controllers

import it.polito.wa2.ticket_catalogue_service.dtos.OrderDTO
import it.polito.wa2.ticket_catalogue_service.exceptions.OrderNotFoundException
import it.polito.wa2.ticket_catalogue_service.services.OrderServiceImpl
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/orders")
class OrderController {
    @Autowired
    private lateinit var orderService: OrderServiceImpl

    @GetMapping(produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun getLoggedUserOrders(
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<Flow<OrderDTO>> {
        val retrievedOrders = orderService.getAllOrdersByUsername(loggedUser.name)
        return ResponseEntity.ok(retrievedOrders)
    }

    @GetMapping("/{orderId}", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun getLoggedUserSpecificOrder(
        @PathVariable("orderId") orderId: Long,
        @RequestHeader("Authorization") authorizationHeader: String,
        loggedUser: Principal
    ): ResponseEntity<OrderDTO> {
        return try {
            val retrievedOrder = orderService.getOrderByIdAndUsername(orderId, loggedUser.name, authorizationHeader)
            ResponseEntity.ok(retrievedOrder)
        } catch (ex: OrderNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }
}
