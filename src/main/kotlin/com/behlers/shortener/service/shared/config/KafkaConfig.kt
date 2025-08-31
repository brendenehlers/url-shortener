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

@Configuration
class KafkaConfig {

  companion object {
    const val ANALYTICS_TOPIC_NAME = "ANALYTICS-TOPIC"
  }

  @Bean fun analyticsTopic(): NewTopic = TopicBuilder.name(ANALYTICS_TOPIC_NAME).build()

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

  @Bean
  fun urlAnalyticsMessageContainerFactory(
    urlAnalyticsMessageConsumerFactory: ConsumerFactory<String, UrlAnalyticsMessage>
  ): ConcurrentKafkaListenerContainerFactory<String, UrlAnalyticsMessage> =
    ConcurrentKafkaListenerContainerFactory<String, UrlAnalyticsMessage>().apply {
      this.consumerFactory = urlAnalyticsMessageConsumerFactory
    }
}
