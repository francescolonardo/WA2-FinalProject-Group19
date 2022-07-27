package it.polito.wa2.ticket_catalogue_service.repositories

import it.polito.wa2.ticket_catalogue_service.entities.Order
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
interface OrderRepository : CoroutineCrudRepository<Order, Long> {
    fun findByUsername(username: String): Flow<Order>
    suspend fun findByIdAndUsername(orderId: Long, username: String): Order?

    @Query("SELECT all FROM Order o WHERE o.orderdate  > :start and  o.orderdate  < :end ")
    fun findAllOrdersByDate(
        @Param("start") start: Timestamp?,
        @Param("end") end: Timestamp?
    ): Flow<Order>


    @Query("SELECT all FROM Order o WHERE o.username = :username and o.orderdate  > :start and  o.orderdate  < :end ")
    fun findAllUserOrdersByDate(
        @Param("start") start: Timestamp?,
        @Param("end") end: Timestamp?,
        username: String
    ): Flow<Order>
}
