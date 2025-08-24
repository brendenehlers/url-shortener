package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.domain.UrlNotFoundException
import com.behlers.shortener.service.url.repository.UrlRepository
import java.security.MessageDigest
import org.springframework.stereotype.Service

@Service
class UrlService(private val urlRepository: UrlRepository) {

  companion object {
    val md5 = MessageDigest.getInstance("MD5")
  }

  // TODO replace this with actual algorithm
  private var tempCode = "1"

  fun getUrl(shortCode: String): Url {
    return urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
  }

  fun createUrl(longUrl: String): Url {
    return urlRepository.save(Url(createShortCode(longUrl), longUrl))
  }

  fun updateUrl(shortCode: String, longUrl: String): Url {
    val existingUrl =
      urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
    return urlRepository.save(existingUrl.apply { this.longUrl = longUrl })
  }

  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }

  private fun createShortCode(longUrl: String): String {
    val digest = md5.digest(longUrl.toByteArray())
    return encodeToBase62(byteArrayToLong(digest))
  }

  private fun byteArrayToLong(arr: ByteArray): Long {
    TODO("TODO implement this")
  }

  private fun encodeToBase62(number: Long): String {
    val alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val base = alphabet.length
    var num = number
    val encoded = StringBuilder()

    if (num == 0L) {
      return alphabet[0].toString() // Handle zero case
    }

    while (num > 0) {
      val remainder = (num % base).toInt()
      encoded.insert(0, alphabet[remainder])
      num /= base
    }
    return encoded.toString()
  }
}
