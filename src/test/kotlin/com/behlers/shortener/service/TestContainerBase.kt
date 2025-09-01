package com.behlers.shortener.service

import java.time.Duration
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.ConfluentKafkaContainer
import org.testcontainers.utility.DockerImageName

@Testcontainers
open class TestContainerBase {
  companion object {
    val network = Network.newNetwork()

    val db: PostgreSQLContainer<*> =
      PostgreSQLContainer("postgres:latest")
        .waitingFor(Wait.forListeningPort())
        .withStartupTimeout(Duration.ofSeconds(30))
        .withNetwork(network)

    val kafka: ConfluentKafkaContainer =
      ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
        .waitingFor(Wait.forListeningPort())
        .withStartupTimeout(Duration.ofSeconds(30))
        .withListener("kafka:29092")
        .withNetwork(network)

    val schemaRegistry: GenericContainer<*> = GenericContainer(DockerImageName.parse("confluentinc/cp-schema-registry:latest"))
      .waitingFor(Wait.forHttp("/subjects").forStatusCode(200))
      .withStartupTimeout(Duration.ofSeconds(30))
      .dependsOn(kafka)
      .withNetwork(network)
      .withExposedPorts(8081)
      .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafka:29092")
      .withEnv("SCHEMA_REGISTRY_KAFKASTORE_SECURITY_PROTOCOL", "PLAINTEXT")
      .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
      .withEnv("SCHEMA_REGISTRY_SCHEMA_REGISTRY_INTER_INSTANCE_PROTOCOL", "http")
      .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")

    @BeforeAll
    @JvmStatic
    fun start() {
      db.start()
      kafka.start()
      schemaRegistry.start()
    }

    @AfterAll
    @JvmStatic
    fun stop() {
      db.stop()
      kafka.stop()
      schemaRegistry.stop()
    }

    @DynamicPropertySource
    @JvmStatic
    fun configureProperties(registry: DynamicPropertyRegistry) {
      // flyway configuration
      registry.add("spring.flyway.user") { db.username }
      registry.add("spring.flyway.password") { db.password }
      registry.add("spring.flyway.url") { db.jdbcUrl }
      registry.add("spring.flyway.default-schema") { "public" }

      // datasource configuration
      registry.add("spring.datasource.url") { db.jdbcUrl }
      registry.add("spring.datasource.username") { db.username }
      registry.add("spring.datasource.password") { db.password }

      // kafka configuration
      registry.add("spring.kafka.bootstrap-servers") { kafka.bootstrapServers }
      registry.add("spring.kafka.security.protocol") { "PLAINTEXT" }

      // schema registry configuration
      val schemaRegistryUrl = "http://${schemaRegistry.host}:${schemaRegistry.firstMappedPort}"
      registry.add("spring.kafka.producer.properties.schema.registry.url") { schemaRegistryUrl }
      registry.add("spring.kafka.consumer.properties.schema.registry.url") { schemaRegistryUrl }
    }
  }
}
