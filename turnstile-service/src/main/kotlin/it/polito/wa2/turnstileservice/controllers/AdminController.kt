package it.polito.wa2.turnstileservice.controllers

import it.polito.wa2.turnstileservice.dtos.*
import it.polito.wa2.turnstileservice.services.TurnstileService
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Timestamp

@RestController
@RequestMapping("/admin/")
class AdminController {
    @Autowired
    lateinit var turnstileService: TurnstileService

    @GetMapping("/turnstiles/{turnstileId}/info")
    suspend fun turnstileInfoGet(
        @PathVariable("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileDetailsDTO?> {
        val retrievedTurnstileDetailsDTO =
            turnstileService.getTurnstileDetails(turnstileId)
        return ResponseEntity.ok(retrievedTurnstileDetailsDTO)
    }

    @PostMapping("/turnstiles/info")
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

    @GetMapping("/turnstiles/{turnstileId}/validations", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    suspend fun turnstileValidationsGet(
        @PathVariable("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Flow<TurnstileValidationDTO>> {
        val retrievedTurnstileValidations =
            turnstileService.getTurnstileValidationsByTurnstileId(turnstileId)
        return ResponseEntity.ok(retrievedTurnstileValidations)
    }

    @GetMapping("/turnstiles/validations/{ticketId}")
    suspend fun turnstileTicketValidationGet(
        @PathVariable("ticketId") ticketId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<TurnstileValidationDTO?> {
        val turnstileValidationDTO =
            turnstileService.getTurnstilesValidationByTicketId(ticketId)
        return ResponseEntity.ok(turnstileValidationDTO)
    }

    @GetMapping("/turnstiles/activity")
    suspend fun allTurnstilesTransitCountGet(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val activity = HashMap<String, Long>()
        activity["count"] = turnstileService.getAllTurnstilesActivity()
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/turnstiles/{turnstileId}/activity")
    suspend fun turnstileTransitCountGet(
        @PathVariable("turnstileId") turnstileId: Long,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val activity = HashMap<String, Long>()
        activity["count"] = turnstileService.getTurnstileActivity(turnstileId)
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/turnstiles/activity/date")
    suspend fun allTurnstilesTransitCountPeriodGet(
        @RequestParam("start") startDate: Date,
        @RequestParam("end") endDate: Date,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val startDateTime = Timestamp(startDate.time).toLocalDateTime()
        val endDateTime = Timestamp(endDate.time).toLocalDateTime()
        val activity = HashMap<String, Long>()
        activity["count"] =
            turnstileService.getAllTurnstilesActivityPeriod(startDateTime, endDateTime)
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/turnstiles/{turnstileId}/activity/date")
    suspend fun turnstileTransitCountPeriodGet(
        @PathVariable("turnstileId") turnstileId: Long,
        @RequestParam("start") startDate: Date,
        @RequestParam("end") endDate: Date,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val startDateTime = Timestamp(startDate.time).toLocalDateTime()
        val endDateTime = Timestamp(endDate.time).toLocalDateTime()
        val activity = HashMap<String, Long>()
        activity["count"] =
            turnstileService.getTurnstileActivityPeriod(turnstileId, startDateTime, endDateTime)
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/users/{username}/activity")
    suspend fun userTransitCountGet(
        @PathVariable("username") username: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val activity = HashMap<String, Long>()
        activity["count"] = turnstileService.getUserActivity(username)
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/users/{username}/activity/date")
    suspend fun userTransitCountPeriodGet(
        @PathVariable("username") username: String,
        @RequestParam("start") startDate: Date,
        @RequestParam("end") endDate: Date,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Any> {
        val startDateTime = Timestamp(startDate.time).toLocalDateTime()
        val endDateTime = Timestamp(endDate.time).toLocalDateTime()
        val activity = HashMap<String, Long>()
        activity["count"] = turnstileService.getUserActivityPeriod(username, startDateTime, endDateTime)
        return ResponseEntity.ok(activity)
    }

    @GetMapping("/users/{username}/transits", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun allUserTransitsGet(
        @PathVariable("username") username: String,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Flow<TurnstileValidationDTO?>> {
        return ResponseEntity.ok(turnstileService.getAllUserTransits(username))
    }
}
