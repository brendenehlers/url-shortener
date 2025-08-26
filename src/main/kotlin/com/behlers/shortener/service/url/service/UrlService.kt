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
    var shortCode = encodingService.encode(longUrl.toString())

    var i = 0
    while (urlRepository.existsUrlByShortCode(shortCode)) {
      shortCode = encodingService.encode(longUrl.toString(), (i++).toString())
    }

    return urlRepository.save(UrlEntity(shortCode, longUrl.toString()))
  }

  fun updateUrl(shortCode: String, longUrl: URL): UrlEntity {
    val existingUrl =
      urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
    return urlRepository.save(existingUrl.apply { this.longUrl = longUrl.toString() })
  }

  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }
}
