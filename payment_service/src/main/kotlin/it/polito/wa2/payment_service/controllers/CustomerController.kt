package it.polito.wa2.payment_service.controllers

import it.polito.wa2.payment_service.dtos.TransactionDTO
import it.polito.wa2.payment_service.services.TransactionServiceImpl
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController {
   @Autowired
   private lateinit var transactionService: TransactionServiceImpl

    @GetMapping("/transactions", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun transactions(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<Flow<TransactionDTO>> {
        val loggedUsername: String = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok(transactionService.findAllByUsername(loggedUsername))
    }
}
