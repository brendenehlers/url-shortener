package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.TestContainerBase
import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.UrlEntity
import com.behlers.shortener.service.url.repository.UrlRepository
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlCrudControllerTest : TestContainerBase() {

  @Autowired lateinit var urlRepository: UrlRepository

  @Autowired lateinit var webClient: WebTestClient

  @AfterEach
  fun cleanup() {
    urlRepository.deleteAll()
  }

  @Test
  fun `creates url after calling createUrl`() {
    val longUrl = "https://test.com"
    webClient
      .post()
      .uri("/api/v1/url")
      .bodyValue(CreateUrlRequestBody(longUrl))
      .exchange()
      .expectBody(UrlEntity::class.java)
      .value {
        it.shortCode.shouldNotBeNull()
        urlRepository.getUrlByShortCode(it.shortCode).shouldNotBeNull()

        it.longUrl shouldBe longUrl
        it.createdAt.shouldNotBeNull()
        it.updatedAt.shouldNotBeNull()
      }
      .returnResult()
  }

  @Test
  fun `gets url from database after calling getUrl`() {
    val longUrl = "https://test.com"
    val shortCode = "abcdefgh"
    val now = Instant.now()

    urlRepository.save(UrlEntity(shortCode, longUrl, now, now))

    webClient
      .get()
      .uri("/api/v1/url/$shortCode")
      .exchange()
      .expectBody(UrlEntity::class.java)
      .value {
        it.shortCode shouldBe shortCode
        it.longUrl shouldBe longUrl
        it.createdAt shouldBeBefore now.plusSeconds(1)
        it.createdAt shouldBeAfter now.minusSeconds(1)
        it.updatedAt shouldBeBefore now.plusSeconds(1)
        it.updatedAt shouldBeAfter now.minusSeconds(1)
      }
  }
}
