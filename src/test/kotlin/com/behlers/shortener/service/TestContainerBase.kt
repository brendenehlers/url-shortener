package com.behlers.shortener.service

import java.time.Duration
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class TestContainerBase {
  companion object {
    val db: PostgreSQLContainer<*> =
      PostgreSQLContainer("postgres:latest")
        .waitingFor(Wait.forListeningPort())
        .withStartupTimeout(Duration.ofSeconds(30))

    @BeforeAll
    @JvmStatic
    fun start() {
      db.start()
    }

    @AfterAll
    @JvmStatic
    fun stop() {
      db.stop()
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
    }
  }
}
