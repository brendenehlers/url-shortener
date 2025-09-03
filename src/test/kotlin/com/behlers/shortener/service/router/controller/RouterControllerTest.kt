package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.TestContainerBase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouterControllerTest : TestContainerBase() {

  @Autowired lateinit var webClient: WebTestClient

  @Test
  fun `router controller returns 400 when invalid short code`() {
    webClient.get().uri("/invalid").exchange().expectStatus().is4xxClientError
  }
}
