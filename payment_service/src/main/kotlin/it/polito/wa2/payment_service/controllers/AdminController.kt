package it.polito.wa2.payment_service.controllers

import it.polito.wa2.payment_service.dtos.TransactionDTO
import it.polito.wa2.payment_service.services.TransactionServiceImpl
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {
    @Autowired
    private lateinit var transactionService: TransactionServiceImpl

    @GetMapping("/admin/transactions", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun transactions(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Flow<TransactionDTO>> {
        return ResponseEntity.ok(transactionService.findAll())
    }
}
