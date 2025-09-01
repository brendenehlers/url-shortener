package com.behlers.shortener.service.shared.service

interface EncodingService {
  fun encode(input: String, salt: String? = null): String

  fun isValidEncoding(input: String): Boolean
}
