package com.behlers.shortener.service.shared.service

import com.behlers.shortener.service.shared.config.KafkaConfig
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessagingService(private val kafkaTemplate: KafkaTemplate<String, UrlAnalyticsMessage>) {
  fun sendAnalyticsMessage(message: UrlAnalyticsMessage) {
    kafkaTemplate.send(KafkaConfig.ANALYTICS_TOPIC_NAME, message.shortCode, message)
  }
}
