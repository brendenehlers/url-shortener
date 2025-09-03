package com.behlers.shortener.service.shared.controller

import com.behlers.shortener.service.TestContainerBase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest : TestContainerBase() {

  @Autowired lateinit var webClient: WebTestClient

  @Test
  fun `GET root redirects to swagger ui`() {
    webClient
      .get()
      .uri("/")
      .exchange()
      .expectStatus()
      .isFound
      .expectHeader()
      .valueMatches("Location", ".*/swagger-ui.html")
  }
}
