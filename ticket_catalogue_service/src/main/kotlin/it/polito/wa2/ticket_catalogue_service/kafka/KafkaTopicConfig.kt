package it.polito.wa2.ticket_catalogue_service.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaTopicConfig {
    @Bean
    fun createPaymentRequestsTopic(): NewTopic {
        return NewTopic(
            "\${topics.payment-requests-topic.name}",
            1,
            1
        )
    }

    @Bean
    fun createPaymentResponsesTopic(): NewTopic {
        return NewTopic(
            "\${topics.payment-responses-topic.name}",
            1,
            1
        )
    }
}
