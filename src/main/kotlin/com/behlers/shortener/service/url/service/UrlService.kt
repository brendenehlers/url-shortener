package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.UrlEntity
import com.behlers.shortener.service.url.domain.UrlNotFoundException
import com.behlers.shortener.service.url.repository.UrlRepository
import java.net.URL
import org.springframework.stereotype.Service

@Service
class UrlService(
  private val urlRepository: UrlRepository,
  private val encodingService: EncodingService,
) {

  fun getUrl(shortCode: String): UrlEntity {
    return urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
  }

  fun createUrl(longUrl: URL): UrlEntity {
    val shortCode = encodingService.encode(longUrl.toString())

    if (urlRepository.existsUrlByShortCode(shortCode)) {
      return getUrl(shortCode)
    }

    return urlRepository.save(UrlEntity(shortCode, longUrl.toString()))
  }

  fun updateUrl(shortCode: String, longUrl: String): UrlEntity {
    val existingUrl =
      urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
    return urlRepository.save(existingUrl.apply { this.longUrl = longUrl })
  }

  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }
}
