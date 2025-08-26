package com.behlers.shortener.service.url.service

import java.math.BigInteger
import java.security.MessageDigest
import org.springframework.stereotype.Service

@Service
class EncodingService {
  companion object {
    const val SHORT_CODE_LENGTH = 8
    const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    val md5: MessageDigest = MessageDigest.getInstance("MD5")
  }

  fun encode(input: String, salt: String? = null): String {
    var inputStr = input
    salt?.let { inputStr += salt }
    val digest = md5.digest(inputStr.toByteArray())
    return encodeToBase62(digest)
  }

  fun isValidEncoding(input: String): Boolean {
    if (input.length != SHORT_CODE_LENGTH) return false
    return input.all { ALPHABET.contains(it) }
  }

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
