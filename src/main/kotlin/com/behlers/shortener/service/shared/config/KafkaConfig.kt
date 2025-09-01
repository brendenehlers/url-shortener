package com.behlers.shortener.service.shared.config

import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessage
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

/**
 * Kafka configuration for analytics topic and consumer setup.
 *
 * Provides beans for topic creation, consumer factory, and listener container factory
 * for UrlAnalyticsMessage protobuf messages.
 */
@Configuration
class KafkaConfig {

  companion object {
    /**
     * Name of the analytics topic for URL analytics messages.
     */
    const val ANALYTICS_TOPIC_NAME = "ANALYTICS-TOPIC"
  }

  /**
   * Creates the analytics topic if it does not exist.
   *
   * @return NewTopic instance for analytics.
   */
  @Bean fun analyticsTopic(): NewTopic = TopicBuilder.name(ANALYTICS_TOPIC_NAME).build()

  /**
   * Configures a Kafka consumer factory for UrlAnalyticsMessage protobuf messages.
   *
   * @param props Kafka properties from Spring Boot.
   * @return ConsumerFactory for String keys and UrlAnalyticsMessage values.
   */
  @Bean
  fun urlAnalyticsMessageConsumerFactory(
    props: KafkaProperties
  ): ConsumerFactory<String, UrlAnalyticsMessage> =
    DefaultKafkaConsumerFactory(
      props.buildConsumerProperties().apply {
        this[KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE] =
          UrlAnalyticsMessage::class.qualifiedName
      }
    )

  /**
   * Provides a Kafka listener container factory for UrlAnalyticsMessage consumers.
   *
   * @param urlAnalyticsMessageConsumerFactory Consumer factory for analytics messages.
   * @return ConcurrentKafkaListenerContainerFactory for analytics topic.
   */
  @Bean
  fun urlAnalyticsMessageContainerFactory(
    urlAnalyticsMessageConsumerFactory: ConsumerFactory<String, UrlAnalyticsMessage>
  ): ConcurrentKafkaListenerContainerFactory<String, UrlAnalyticsMessage> =
    ConcurrentKafkaListenerContainerFactory<String, UrlAnalyticsMessage>().apply {
      this.consumerFactory = urlAnalyticsMessageConsumerFactory
    }
}
