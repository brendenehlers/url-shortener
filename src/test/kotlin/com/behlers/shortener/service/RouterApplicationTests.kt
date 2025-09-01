package com.behlers.shortener.service

import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class RouterApplicationTests : TestContainerBase() {

  @Test fun contextLoads() {}

  @Test
  fun `db is live`() {
    assertTrue { db.isRunning }
  }

  @Test
  fun `kafka is live`() {
    assertTrue { kafka.isRunning }
  }

  @Test
  fun `schema registry is live`() {
    assertTrue { schemaRegistry.isRunning }
  }
}
