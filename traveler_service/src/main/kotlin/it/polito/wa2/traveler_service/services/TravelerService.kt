package it.polito.wa2.traveler_service.services

import it.polito.wa2.traveler_service.dtos.TicketPurchasedDTO
import it.polito.wa2.traveler_service.dtos.UserDetailsDTO
import it.polito.wa2.traveler_service.entities.TicketPurchased

interface TravelerService {
    fun getProfileById(id: Long): UserDetailsDTO?
    fun getProfileByUsername(username: String): UserDetailsDTO?
    fun getAllProfiles(): List<UserDetailsDTO>?
    fun getTicketsById(id: Long): List<TicketPurchasedDTO>?
    fun getTicketsByUsername(username: String): List<TicketPurchasedDTO>?
    fun updateProfileByUsername(username: String, dateOfBirth: String, address: String, telephoneNumber: String): UserDetailsDTO?
    fun purchaseTicketsByUsername(username: String, quantity: Int, zones: String): List<TicketPurchasedDTO>?
    fun updateUsedPropertyById(ticket: TicketPurchased): Boolean
    fun getTicketDetailById(id:Long) : TicketPurchased?
}
