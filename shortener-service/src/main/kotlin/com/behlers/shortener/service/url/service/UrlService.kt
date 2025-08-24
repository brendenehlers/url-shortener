package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.domain.UrlNotFoundException
import com.behlers.shortener.service.url.repository.UrlRepository
import org.springframework.stereotype.Service

@Service
class UrlService(private val urlRepository: UrlRepository) {

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
    val newCode = tempCode
    tempCode = (tempCode.toInt() + 1).toString()
    return newCode
  }
}
