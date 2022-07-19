package it.polito.wa2.ticket_catalogue_service.services

import it.polito.wa2.ticket_catalogue_service.dtos.BillingInformationDTO
import it.polito.wa2.ticket_catalogue_service.dtos.OrderDTO
import kotlinx.coroutines.flow.Flow

interface OrderService {
    suspend fun addNewOrder(billingInformationDTO: BillingInformationDTO, username: String, authorizationHeader: String): Long?
    suspend fun getOrderByIdAndUsername(orderId: Long, username: String, authorizationHeader: String): OrderDTO?
    fun getAllOrdersByUsername(username: String): Flow<OrderDTO>
    fun getAllOrders(): Flow<OrderDTO>
    suspend fun getAllOrdersByUserId(userId: Long, authorizationHeader: String): Flow<OrderDTO>
    fun updateOrderByTransactionInfo(transactionJson: String)
}
