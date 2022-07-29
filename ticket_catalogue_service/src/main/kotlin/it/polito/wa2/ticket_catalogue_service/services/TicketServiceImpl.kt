package it.polito.wa2.ticket_catalogue_service.services

import it.polito.wa2.ticket_catalogue_service.dtos.TicketDTO
import it.polito.wa2.ticket_catalogue_service.dtos.toDTO
import it.polito.wa2.ticket_catalogue_service.entities.Ticket
import it.polito.wa2.ticket_catalogue_service.repositories.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl : TicketService {
    @Autowired
    private lateinit var ticketRepository: TicketRepository

    override fun getAllTickets(): Flow<TicketDTO> {
        return ticketRepository.findAll()
            .map { ticket -> ticket.toDTO() }
    }

    override suspend fun addNewTicket(newTicketDTO: TicketDTO): TicketDTO {
        return ticketRepository.save(
            Ticket(
                null,
                newTicketDTO.type,
                newTicketDTO.validityZones,
                newTicketDTO.price,
                newTicketDTO.minAge,
                newTicketDTO.maxAge
            )
        ).toDTO()
    }
}
