package it.polito.wa2.login_service.interceptors

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.ConsumptionProbe
import io.github.bucket4j.Refill
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RateLimitInterceptor: HandlerInterceptor {

    private val bandwidth: Bandwidth = Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(1)))
    private val bucket: Bucket = Bucket.builder().addLimit(bandwidth).build()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val probe: ConsumptionProbe = bucket.tryConsumeAndReturnRemaining(1)
        return if(probe.isConsumed){
            response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
            true
        }else {
            val waitForRefill: Long = probe.nanosToWaitForRefill / 1_000_000_000
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", waitForRefill.toString())
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota")
            false
        }

    }
}
