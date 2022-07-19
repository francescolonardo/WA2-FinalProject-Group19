package it.polito.wa2.ticket_catalogue_service.services

import it.polito.wa2.ticket_catalogue_service.dtos.TicketDTO
import kotlinx.coroutines.flow.Flow

interface TicketService {
    fun getAllTickets(): Flow<TicketDTO>
    suspend fun addNewTicket(newTicketDTO: TicketDTO): TicketDTO
}
