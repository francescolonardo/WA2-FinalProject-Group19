package it.polito.wa2.ticket_catalogue_service.services

import com.fasterxml.jackson.databind.ObjectMapper
import it.polito.wa2.ticket_catalogue_service.dtos.*
import it.polito.wa2.ticket_catalogue_service.entities.Order
import it.polito.wa2.ticket_catalogue_service.entities.OrderStatus
import it.polito.wa2.ticket_catalogue_service.exceptions.OrderNotFoundException
import it.polito.wa2.ticket_catalogue_service.exceptions.TicketNotFoundException
import it.polito.wa2.ticket_catalogue_service.repositories.OrderRepository
import it.polito.wa2.ticket_catalogue_service.repositories.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

@Service
class OrderServiceImpl : OrderService {
    private val travelerWebClient = WebClient.create("http://localhost:8282") // traveler-service
    @Autowired
    private lateinit var ticketRepository: TicketRepository
    @Autowired
    private lateinit var orderRepository: OrderRepository
    @Value("\${jwt.authorization.http-header-name}")
    private lateinit var jwtHttpHeaderName: String
    @Value("\${topics.payment-requests-topic.name}")
    private lateinit var paymentRequestsTopicName: String
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    override suspend fun addNewOrder(
        billingInformationDTO: BillingInformationDTO,
        username: String,
        authorizationHeader: String
    ) : Long? {
        val ticket = ticketRepository.findById(billingInformationDTO.ticketId)
            ?: throw TicketNotFoundException("Ticket not found with ticketId=${billingInformationDTO.ticketId}")
        if (ticket.minAge != null || ticket.maxAge != null) {
            val userProfile: UserDetailsDTO = travelerWebClient
                .get()
                .uri("/my/profile")
                .header(jwtHttpHeaderName, authorizationHeader)
                .retrieve()
                .onStatus({ !it.equals(HttpStatus.OK) }) { resp ->
                    resp.bodyToMono(String::class.java).map { Exception(it) }
                }
                .awaitBody()
            // TODO: comment this (just for tests)
            /*
            val birthDate = userProfile.dateOfBirth!!
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val age = Period.between(birthDate, LocalDate.now()).years
            if (ticket.minAge != null && ticket.minAge > age)
                return null
            if (ticket.maxAge != null && ticket.maxAge < age)
                return null
            */
        }
        val newOrder = orderRepository.save(
            Order(
                null,
                billingInformationDTO.ticketId,
                billingInformationDTO.ticketsQuantity,
                OrderStatus.PENDING,
                username,
                false
            )
        )
        kafkaTemplate.send(
            paymentRequestsTopicName,
            PaymentDTO(
                billingInformationDTO.ticketsQuantity * ticket.price,
                billingInformationDTO.cardHolder,
                billingInformationDTO.cardNumber,
                billingInformationDTO.cardExpDate,
                billingInformationDTO.cardCvv,
                newOrder.id!!,
                username
            )
        )
        return newOrder.id
    }

    override suspend fun getOrderByIdAndUsername(orderId: Long, username: String, authorizationHeader: String): OrderDTO? {
        val order = orderRepository.findByIdAndUsername(orderId, username)
            ?: throw OrderNotFoundException("Order not found with orderId=${orderId}, username=${username}")
        val ticket = ticketRepository.findById(order.ticketId)
        if (!order.purchased && order.status == OrderStatus.COMPLETED) {
            buyOrderedTickets(order.quantity, ticket!!.validityZones, authorizationHeader)
            order.purchased = true
            orderRepository.save(order)
        }
        return order.toDTO()
    }

    override fun getAllOrdersByUsername(username: String): Flow<OrderDTO> {
        return orderRepository.findByUsername(username)
            .map { order -> order.toDTO() }
    }

    override fun getAllOrders(): Flow<OrderDTO> {
        return orderRepository.findAll()
            .map { order -> order.toDTO() }
    }

    private suspend fun retrieveUsernameByUserId(userId: Long, authorizationHeader: String): String {
        val userProfile: UserDetailsDTO = travelerWebClient
            .get()
            .uri("/admin/traveler/${userId}/profile")
            .accept(MediaType.APPLICATION_JSON)
            .header(jwtHttpHeaderName, authorizationHeader)
            .retrieve()
            .onStatus({ !it.equals(HttpStatus.OK) }) { resp ->
                resp.bodyToMono(String::class.java).map { Exception(it) }
            }
            .awaitBody()
        return userProfile.username
    }

    override suspend fun getAllOrdersByUserId(userId: Long, authorizationHeader: String): Flow<OrderDTO> {
        val username = retrieveUsernameByUserId(userId, authorizationHeader)
        return orderRepository.findByUsername(username)
            .map { order -> order.toDTO() }
    }

    suspend fun buyOrderedTickets(ticketsQuantity: Int, ticketValidityZones: String, authorizationHeader: String) {
        val buyTicketsRequestDTO = BuyTicketsRequestDTO(
            "buy_tickets",
            ticketsQuantity,
            ticketValidityZones
        )
        val boughtOrderedTickets: List<TicketPurchasedDTO> = travelerWebClient
            .post()
            .uri("/my/tickets")
            .accept(MediaType.APPLICATION_JSON)
            .header(jwtHttpHeaderName, authorizationHeader)
            .body(BodyInserters.fromValue(buyTicketsRequestDTO))
            .retrieve()
            .onStatus({ !it.equals(HttpStatus.OK) }) { resp ->
                resp.bodyToMono(String::class.java).map { Exception(it) }
            }
            .awaitBody()
    }

    @KafkaListener(topics = ["\${topics.payment-responses-topic.name}"], groupId = "group-id")
    override fun updateOrderByTransactionInfo(transactionJson: String) {
        val transactionDTO = ObjectMapper().readValue(transactionJson, TransactionDTO::class.java)
        if (transactionDTO.orderStatus == OrderStatus.COMPLETED) {
            runBlocking {
                val order = orderRepository.findById(transactionDTO.orderId)
                    ?: return@runBlocking // TODO: check this
                order.status = transactionDTO.orderStatus
                orderRepository.save(order)
            }
        }
    }
}
