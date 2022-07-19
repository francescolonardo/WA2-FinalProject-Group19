package it.polito.wa2.payment_service.services

import it.polito.wa2.payment_service.dtos.TransactionDTO
import it.polito.wa2.payment_service.dtos.toDTO
import it.polito.wa2.payment_service.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl:TransactionService {
    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    override fun findAll(): Flow<TransactionDTO>{
        return transactionRepository.findAll().map { t -> t.toDTO() }
    }

    override fun findAllByUsername(username: String): Flow<TransactionDTO>{
        return transactionRepository.findAllByUsername(username).map { t -> t.toDTO() }
    }
}
