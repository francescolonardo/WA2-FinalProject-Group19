package it.polito.wa2.ticket_catalogue_service.repositories

import it.polito.wa2.ticket_catalogue_service.entities.Order
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : CoroutineCrudRepository<Order, Long> {
    fun findByUsername(username: String): Flow<Order>
    suspend fun findByIdAndUsername(orderId: Long, username: String): Order?

    @Query("SELECT all FROM Order o WHERE o.orderdate between '2022-01-01' and '2022-12-30'")
    fun findOrdersIn2022(): Flow<Order?>?

    @Query("SELECT all FROM Order o WHERE o.orderdate > :constraint ")
    fun findOrdersByDate(
        @Param("constraint") start: Date?,
    ): Flow<Order?>?
}
