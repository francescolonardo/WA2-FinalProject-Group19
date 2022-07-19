package it.polito.wa2.payment_service.services

import it.polito.wa2.payment_service.dtos.TransactionDTO
import kotlinx.coroutines.flow.Flow

interface TransactionService {
    fun findAll(): Flow<TransactionDTO>
    fun findAllByUsername(username: String): Flow<TransactionDTO>
}
