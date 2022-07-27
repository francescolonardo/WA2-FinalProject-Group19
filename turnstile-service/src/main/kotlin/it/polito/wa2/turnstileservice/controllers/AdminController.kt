package it.polito.wa2.turnstileservice.controllers

import it.polito.wa2.turnstileservice.dtos.TurnstileActivityDTO
import it.polito.wa2.turnstileservice.dtos.TurnstileDTO
import it.polito.wa2.turnstileservice.dtos.TurnstileValidationDTO
import it.polito.wa2.turnstileservice.dtos.UserActivityDTO
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

    @GetMapping("/turnstile", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileGet(
        @RequestParam("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileDTO> {
        return ResponseEntity.ok(turnstileService.getTurnstileById(turnstileId))
    }

    @GetMapping("/turnstileValidation", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileValidationGet(
        @RequestParam("ticketId") ticketId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileValidationDTO?> {
        val turnstileValidationDTO: TurnstileValidationDTO? = turnstileService.getTurnstileValidationByTicketId(ticketId)
        return if(turnstileValidationDTO != null)
            ResponseEntity.ok(turnstileValidationDTO)
        else
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

    @PostMapping("/registerTurnstile", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun registerTurnstilePost(
        @RequestBody turnstileDTO: TurnstileDTO,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileDTO?> {
        try {
            turnstileService.addTurnstile(turnstileDTO)
        }catch(ex: Exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(turnstileDTO)
    }

    @GetMapping("/transitCount", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileTransitCountGet(
        @RequestParam("turnstileId"),
        turnstileId: Long,
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
