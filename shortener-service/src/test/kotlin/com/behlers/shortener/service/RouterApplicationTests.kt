package com.behlers.shortener.service

import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class RouterApplicationTests : TestContainerBase() {

  @Test fun contextLoads() {}

  @Test
  fun `db is live`() {
    assertTrue(db.isRunning)
  }
}
