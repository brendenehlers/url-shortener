package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.TestContainerBase
import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.UrlEntity
import com.behlers.shortener.service.url.repository.UrlRepository
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

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
}
