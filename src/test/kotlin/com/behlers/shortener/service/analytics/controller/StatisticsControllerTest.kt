package com.behlers.shortener.service.analytics.controller

import com.behlers.shortener.service.TestContainerBase
import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import com.behlers.shortener.service.shared.domain.UrlEntity
import com.behlers.shortener.service.shared.repository.UrlRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatisticsControllerTest : TestContainerBase() {

  @Autowired lateinit var urlStatsRepository: UrlStatsRepository

  @Autowired lateinit var urlRepository: UrlRepository

  @Autowired lateinit var webClient: WebTestClient

  @AfterEach
  fun cleanup() {
    urlStatsRepository.deleteAll()
    urlRepository.deleteAll()
  }

  @Test
  fun `gets most hits sorted descending`() {
    // ensure corresponding urls exist to satisfy foreign key constraint
    urlRepository.save(UrlEntity("a", "https://a.test"))
    urlRepository.save(UrlEntity("b", "https://b.test"))
    urlRepository.save(UrlEntity("c", "https://c.test"))

    urlStatsRepository.save(UrlStatsEntity("a", 5))
    urlStatsRepository.save(UrlStatsEntity("b", 10))
    urlStatsRepository.save(UrlStatsEntity("c", 1))

    val result =
      webClient
        .get()
        .uri("/api/v1/statistics/mostHits")
        .exchange()
        .expectStatus()
        .is2xxSuccessful
        .expectBodyList(UrlStatsEntity::class.java)
        .returnResult()
        .responseBody

    result!!.size shouldBe 3
    result[0].shortCode shouldBe "b"
    result[1].shortCode shouldBe "a"
    result[2].shortCode shouldBe "c"
  }

  @Test
  fun `gets least hits sorted ascending`() {
    // ensure corresponding urls exist to satisfy foreign key constraint
    urlRepository.save(UrlEntity("a", "https://a.test"))
    urlRepository.save(UrlEntity("b", "https://b.test"))
    urlRepository.save(UrlEntity("c", "https://c.test"))

    urlStatsRepository.save(UrlStatsEntity("a", 5))
    urlStatsRepository.save(UrlStatsEntity("b", 10))
    urlStatsRepository.save(UrlStatsEntity("c", 1))

    val result =
      webClient
        .get()
        .uri("/api/v1/statistics/leastHits")
        .exchange()
        .expectStatus()
        .is2xxSuccessful
        .expectBodyList(UrlStatsEntity::class.java)
        .returnResult()
        .responseBody

    result!!.size shouldBe 3
    result[0].shortCode shouldBe "c"
    result[1].shortCode shouldBe "a"
    result[2].shortCode shouldBe "b"
  }

  @Test
  fun `returns empty list when no stats exist`() {
    val result =
      webClient
        .get()
        .uri("/api/v1/statistics/mostHits")
        .exchange()
        .expectStatus()
        .is2xxSuccessful
        .expectBodyList(UrlStatsEntity::class.java)
        .returnResult()
        .responseBody

    result!!.size shouldBe 0
  }
}
