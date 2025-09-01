package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.TestContainerBase
import com.behlers.shortener.service.shared.domain.UrlEntity
import com.behlers.shortener.service.shared.repository.UrlRepository
import com.behlers.shortener.service.shared.service.EncodingService
import com.behlers.shortener.service.url.domain.api.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.api.DeleteUrlResponseBody
import com.behlers.shortener.service.url.domain.api.UpdateUrlRequestBody
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.Instant
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

  @Autowired lateinit var encodingService: EncodingService

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

    urlRepository.save(UrlEntity(shortCode, longUrl))

    webClient
      .get()
      .uri("/api/v1/url/$shortCode")
      .exchange()
      .expectBody(UrlEntity::class.java)
      .value {
        it.shortCode shouldBe shortCode
        it.longUrl shouldBe longUrl
      }
  }

  @Test
  fun `timestamps are saved to db`() {
    val shortCode = "abcdefgh"
    val now = Instant.now()

    urlRepository.save(UrlEntity(shortCode, "https://test.com", now, now))

    webClient
      .get()
      .uri("/api/v1/url/$shortCode")
      .exchange()
      .expectBody(UrlEntity::class.java)
      .value {
        it.createdAt shouldBeBefore now.plusSeconds(1)
        it.createdAt shouldBeAfter now.minusSeconds(1)
        it.updatedAt shouldBeBefore now.plusSeconds(1)
        it.updatedAt shouldBeAfter now.minusSeconds(1)
      }
  }

  @Test
  fun `updates url in the database after calling updateUrl`() {
    val shortCode = "abcdefgh"
    urlRepository.save(UrlEntity(shortCode, "https://old.test.com"))

    val newLongUrl = "https://new.test.com"
    webClient
      .post()
      .uri("/api/v1/url/$shortCode")
      .bodyValue(UpdateUrlRequestBody(newLongUrl))
      .exchange()
      .expectBody(UrlEntity::class.java)
      .value {
        it.shortCode shouldBe shortCode
        it.longUrl shouldBe newLongUrl
      }
  }

  @Test
  fun `deletes url after calling deleteUrl`() {
    val shortCode = "abcdefgh"
    urlRepository.save(UrlEntity(shortCode, "https://test.com"))

    webClient
      .delete()
      .uri("/api/v1/url/$shortCode")
      .exchange()
      .expectBody(DeleteUrlResponseBody::class.java)
      .value { it.message shouldBe "success" }

    urlRepository.getUrlByShortCode(shortCode).shouldBeNull()
  }

  @Test
  fun `short code with length 7 fails on get`() {
    val shortCode = "1234567"

    webClient.get().uri("/api/v1/url/$shortCode").exchange().expectStatus().is4xxClientError
  }

  @Test
  fun `short code with invalid character fails on get`() {
    val shortCode = "!1234567"

    webClient.get().uri("/api/v1/url/$shortCode").exchange().expectStatus().is4xxClientError
  }

  @Test
  fun `short code with length 7 fails on delete`() {
    val shortCode = "1234567"

    webClient.delete().uri("/api/v1/url/$shortCode").exchange().expectStatus().is4xxClientError
  }

  @Test
  fun `short code with invalid character fails on delete`() {
    val shortCode = "!1234567"

    webClient.delete().uri("/api/v1/url/$shortCode").exchange().expectStatus().is4xxClientError
  }

  @Test
  fun `short code with length 7 fails on update`() {
    val shortCode = "1234567"

    webClient
      .post()
      .uri("/api/v1/url/$shortCode")
      .bodyValue(UpdateUrlRequestBody("https://test.com"))
      .exchange()
      .expectStatus()
      .is4xxClientError
  }

  @Test
  fun `short code with invalid character fails on update`() {
    val shortCode = "!1234567"

    webClient
      .post()
      .uri("/api/v1/url/$shortCode")
      .bodyValue(UpdateUrlRequestBody("https://test.com"))
      .exchange()
      .expectStatus()
      .is4xxClientError
  }

  @Test
  fun `invalid long url fails on update`() {
    val longUrl = "bad-url"

    webClient
      .post()
      .uri("/api/v1/url/12345678")
      .bodyValue(UpdateUrlRequestBody(longUrl))
      .exchange()
      .expectStatus()
      .is4xxClientError
  }

  @Test
  fun `invalid long url fails on create`() {
    val longUrl = "bad-url"

    webClient
      .post()
      .uri("/api/v1/url")
      .bodyValue(CreateUrlRequestBody(longUrl))
      .exchange()
      .expectStatus()
      .is4xxClientError
  }

  @Test
  fun `colliding url creates new short code`() {
    val newUrl = "https://new.test.com"
    val conflictingShortCode = encodingService.encode(newUrl)
    urlRepository.save(
      UrlEntity(shortCode = conflictingShortCode, longUrl = "https://old.test.com")
    )

    webClient
      .post()
      .uri("/api/v1/url")
      .bodyValue(CreateUrlRequestBody(longUrl = newUrl))
      .exchange()
      .expectStatus()
      .is2xxSuccessful
      .expectBody(UrlEntity::class.java)
      .value {
        it.shortCode.shouldNotBeNull()
        it.shortCode shouldNotBe conflictingShortCode
        it.longUrl shouldBe newUrl

        urlRepository.getUrlByShortCode(it.shortCode).shouldNotBeNull()
      }
  }
}
