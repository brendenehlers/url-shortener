package com.behlers.shortener.service.integration

import com.behlers.shortener.service.TestContainerBase
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import com.behlers.shortener.service.analytics.service.UrlStatsService
import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.UrlEntity
import com.behlers.shortener.service.url.repository.UrlRepository
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests : TestContainerBase() {

  @Autowired
  lateinit var webClient: WebTestClient

  @Autowired
  lateinit var urlRepository: UrlRepository

  @Autowired
  lateinit var urlStatsRepository: UrlStatsRepository

  @Test
  fun `creating url via api adds urls stats entry to database`() {
    val response = webClient.post()
      .uri("/api/v1/url")
      .bodyValue(CreateUrlRequestBody("https://test.com"))
      .exchange()
      .expectStatus()
      .is2xxSuccessful
      .expectBody(UrlEntity::class.java)
      .returnResult()
      .responseBody
      ?: fail("didn't receive response body")

    await()
      .pollDelay(Duration.ofSeconds(1))
      .pollInterval(Duration.ofSeconds(1))
      .atMost(Duration.ofSeconds(10))
      .until {
        println("in until loop")
        val urlStats = urlStatsRepository.findByShortCode(response.shortCode)
          ?: fail { "could not find url stats" }

        urlStats.shortCode shouldBe response.shortCode
        urlStats.hits shouldBe 0
        urlStats.lastHit.shouldBeNull()
        true
      }
  }

}