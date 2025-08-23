package com.behlers.shortener.service

import java.time.Duration
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@DataR2dbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

      // r2dbc configuration
      registry.add("spring.r2dbc.url") {
        "r2dbc:postgresql://${db.host}:${db.firstMappedPort}/${db.databaseName}"
      }
      registry.add("spring.r2dbc.username") { db.username }
      registry.add("spring.r2dbc.password") { db.password }
    }
  }
}
