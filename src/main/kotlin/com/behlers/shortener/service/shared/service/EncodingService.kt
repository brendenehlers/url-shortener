package com.behlers.shortener.service.shared.service

/** Service interface for encoding and validating short codes. */
interface EncodingService {

  /**
   * Encodes the input string into a short code.
   *
   * @param input The string to encode.
   * @param salt Optional salt to modify encoding.
   * @return Encoded short code.
   */
  fun encode(input: String, salt: String? = null): String

  /**
   * Validates if the input string is a valid short code.
   *
   * @param input The string to validate.
   * @return True if valid, false otherwise.
   */
  fun isValidEncoding(input: String): Boolean
}
