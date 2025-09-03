package com.behlers.shortener.service.analytics.consumer

import com.behlers.shortener.service.analytics.service.UrlStatsService
import com.behlers.shortener.service.shared.config.KafkaConfig
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessage
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

/**
 * Kafka consumer for URL analytics messages. Listens to analytics topic and delegates create/update
 * actions to UrlStatsService. Throws UnsupportedOperationException for unrecognized message types.
 */
@Service
class AnalyticsConsumer(private val urlStatsService: UrlStatsService) {

  /**
   * Handles incoming Kafka messages for URL analytics. Delegates create or update actions based on
   * message type.
   *
   * @param message the consumed Kafka record containing UrlAnalyticsMessage
   * @throws UnsupportedOperationException if message type is unrecognized or null
   */
  @KafkaListener(
    topics = [KafkaConfig.ANALYTICS_TOPIC_NAME],
    containerFactory = "urlAnalyticsMessageContainerFactory",
  )
  fun handler(message: ConsumerRecord<String, UrlAnalyticsMessage>) {
    when (message.value().type) {
      UrlAnalyticsMessageType.Create -> urlStatsService.createUrlStats(message.value().shortCode)
      UrlAnalyticsMessageType.Update -> urlStatsService.updateUrlStats(message.value().shortCode)
      UrlAnalyticsMessageType.UNRECOGNIZED,
      null -> throw UnsupportedOperationException("invalid url analytics message code")
    }
  }
}
