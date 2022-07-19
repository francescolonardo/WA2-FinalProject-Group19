package it.polito.wa2.login_service.config

import it.polito.wa2.login_service.interceptors.RateLimitInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig: WebMvcConfigurer {

    private val interceptor: RateLimitInterceptor = RateLimitInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(interceptor)
                .addPathPatterns("/user/**")
    }
}
