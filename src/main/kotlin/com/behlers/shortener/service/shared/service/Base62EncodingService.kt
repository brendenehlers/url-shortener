package com.behlers.shortener.service.shared.service

import java.math.BigInteger
import java.security.MessageDigest
import org.springframework.stereotype.Service

/**
 * Implementation of EncodingService using base62 encoding and MD5 hashing.
 *
 * Encodes input strings into fixed-length short codes suitable for URLs. Only characters in the
 * defined alphabet are allowed.
 */
@Service
class Base62EncodingService : EncodingService {
  companion object {
    /** Length of the generated short code. */
    const val SHORT_CODE_LENGTH = 8

    /** Allowed characters for short codes. */
    const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    /** MD5 message digest instance for hashing. */
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
  }

  /**
   * Encodes the input string (optionally salted) into a base62 short code.
   *
   * @param input The string to encode.
   * @param salt Optional salt to modify encoding.
   * @return Encoded short code.
   */
  override fun encode(input: String, salt: String?): String {
    var inputStr = input
    salt?.let { inputStr += salt }
    val digest = md5.digest(inputStr.toByteArray())
    return encodeToBase62(digest)
  }

  /**
   * Validates if the input string is a valid short code.
   *
   * @param input The string to validate.
   * @return True if valid, false otherwise.
   */
  override fun isValidEncoding(input: String): Boolean {
    if (input.length != SHORT_CODE_LENGTH) return false
    return input.all { ALPHABET.contains(it) }
  }

  /**
   * Encodes a byte array to a base62 string of fixed length.
   *
   * @param bytes The byte array to encode.
   * @return Encoded base62 string.
   */
  private fun encodeToBase62(bytes: ByteArray): String {
    var number = BigInteger(1, bytes)

    // constrain url length to N characters
    number = number.mod(BigInteger.valueOf(ALPHABET.length.toLong()).pow(SHORT_CODE_LENGTH))

    // build string by constantly dividing by `ALPHABET.length` and using the remainder to pick a
    // char
    val sb = StringBuilder()
    while (number > BigInteger.ZERO) {
      val (div, rem) = number.divideAndRemainder(BigInteger.valueOf(ALPHABET.length.toLong()))
      number = div
      val remainder = rem.toInt()
      sb.append(ALPHABET[remainder])
    }
    return sb.reverse().toString()
  }
}
