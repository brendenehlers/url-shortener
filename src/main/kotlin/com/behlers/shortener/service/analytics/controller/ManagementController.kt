package com.behlers.shortener.service.analytics.controller

import com.behlers.shortener.service.shared.config.KafkaConfig
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessage
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import com.behlers.shortener.service.shared.domain.urlAnalyticsMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for managing analytics messages.
 * Provides endpoint to publish analytics events to Kafka.
 */
@RestController
@RequestMapping("/analytics")
class ManagementController(private val kafkaTemplate: KafkaTemplate<String, UrlAnalyticsMessage>) {

  /**
   * Publishes an analytics message to Kafka for the given short code and type.
   *
   * @param shortCode the short URL code
   * @param type the type of analytics event
   */
  @PostMapping("/publish/{shortCode}/{type}")
  fun publish(@PathVariable shortCode: String, @PathVariable type: UrlAnalyticsMessageType) {
    kafkaTemplate.send(
      KafkaConfig.ANALYTICS_TOPIC_NAME,
      shortCode,
      urlAnalyticsMessage {
        this.shortCode = shortCode
        this.type = type
      },
    )
  }
}
