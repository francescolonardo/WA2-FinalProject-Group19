package it.polito.wa2.turnstileservice.controllers

import it.polito.wa2.turnstileservice.dtos.*
import it.polito.wa2.turnstileservice.services.TurnstileService
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    lateinit var turnstileService: TurnstileService

    @GetMapping("/turnstile/info")
    suspend fun turnstileInfoGet(
        @RequestParam("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileDetailsDTO?> {
        val retrievedTurnstileDetailsDTO =
            turnstileService.getTurnstileDetails(turnstileId)
        return ResponseEntity.ok(retrievedTurnstileDetailsDTO)
    }

    @PostMapping("/turnstile/info")
    suspend fun turnstileInfoPost(
        @RequestBody turnstileDetailsDTO: TurnstileDetailsDTO,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileDetailsDTO?> {
        return try {
            val newTurnstileDetailsDTO =
                turnstileService.addTurnstileDetails(turnstileDetailsDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(newTurnstileDetailsDTO)
        } catch (ex: Exception) {
            println(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @GetMapping("/turnstileValidation", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileValidationGet(
        @RequestParam("ticketId") ticketId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileValidationDTO?> {
        val turnstileValidationDTO: TurnstileValidationDTO? = turnstileService.getTurnstileValidationByTicketId(ticketId)
        return if (turnstileValidationDTO != null)
            ResponseEntity.ok(turnstileValidationDTO)
        else
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

    @GetMapping("/transitCount", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileTransitCountGet(
        @RequestParam("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getTurnstileTransitCount(turnstileId))
    }

    @GetMapping("/transitCountAll", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun allTurnstilesTransitCountGet(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getAllTurnstilesTransitCount())
    }

    //Used date format -> 2021-07-24T23:29:47.738750
    @GetMapping("/transitCountPeriod", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileTransitCountPeriodGet(
        @RequestParam("turnstileId")
        turnstileId: Long,
        @RequestParam("startPeriod")
        startPeriod: String,
        @RequestParam("endPeriod")
        endPeriod: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getTurnstileTransitCountPeriod(turnstileId, LocalDateTime.parse(startPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(endPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
    }

    @GetMapping("/transitCountPeriodAll", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun allTurnstilesTransitCountPeriodGet(
        @RequestParam("startPeriod")
        startPeriod: String,
        @RequestParam("endPeriod")
        endPeriod: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getAllTurnstilesTransitCountPeriod(LocalDateTime.parse(startPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(endPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
    }

    @GetMapping("/userTransitCount", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun userTransitCountGet(
        @RequestParam("username")
        username: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<UserActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getUserTransitCount(username))
    }

    @GetMapping("/userTransitCountPeriod", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun userTransitCountPeriodGet(
        @RequestParam("username")
        username: String,
        @RequestParam("startPeriod")
        startPeriod: String,
        @RequestParam("endPeriod")
        endPeriod: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<UserActivityDTO?> {
        return ResponseEntity.ok(turnstileService.getUserTransitCountPeriod(username, LocalDateTime.parse(startPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse(endPeriod, DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
    }

    @GetMapping("/userTransits", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun allUserTransitsGet(
        @RequestParam("username")
        username: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Flow<TurnstileValidationDTO?>> {
        return ResponseEntity.ok(turnstileService.getAllUserTransits(username))
    }
}
