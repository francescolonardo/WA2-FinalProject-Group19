package it.polito.wa2.ticket_catalogue_service.repositories

import it.polito.wa2.ticket_catalogue_service.entities.Order
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface OrderRepository : CoroutineCrudRepository<Order, Long> {
    fun findByUsername(username: String): Flow<Order>
    suspend fun findByIdAndUsername(orderId: Long, username: String): Order?
    @Query("SELECT * FROM orders WHERE date_time > :startDate and date_time < :endDate")
    fun findAllOrdersByDate(startDate: Timestamp, endDate: Timestamp): Flow<Order>
    @Query("SELECT * FROM orders WHERE username = :username and date_time > :startDate and date_time < :endDate")
    fun findAllUserOrdersByDate(username: String, startDate: Timestamp, endDate: Timestamp): Flow<Order>
}
