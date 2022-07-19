package it.polito.wa2.payment_service.services

import it.polito.wa2.payment_service.dtos.OrderStatus
import it.polito.wa2.payment_service.dtos.PaymentDTO
import it.polito.wa2.payment_service.dtos.toDTO
import it.polito.wa2.payment_service.entities.Transaction
import it.polito.wa2.payment_service.repositories.TransactionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
 class ConsumerServiceImpl : ConsumerService {
    @Autowired
    lateinit var transactionRepository: TransactionRepository
    @Value("\${topics.payment-responses-topic.name}")
    lateinit var paymentResponsesTopicName: String
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @KafkaListener(topics = ["\${topics.payment-requests-topic.name}"], groupId = "group-id")
    override fun consume(paymentJson: String) {
        val paymentDTO = ObjectMapper().readValue(paymentJson, PaymentDTO::class.java)
        // simulating a verification process on the payment request
        Thread.sleep(5_000) // waiting 5 seconds
        val transaction = Transaction(
            null,
            paymentDTO.orderId,
            OrderStatus.COMPLETED,
            paymentDTO.username,
            paymentDTO.totalCost
        )
        runBlocking {
            transactionRepository.save(transaction)
        }
        kafkaTemplate.send(paymentResponsesTopicName, transaction.toDTO())
    }
}
