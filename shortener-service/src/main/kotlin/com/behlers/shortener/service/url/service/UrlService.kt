package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.domain.UrlNotFoundException
import com.behlers.shortener.service.url.repository.UrlRepository
import org.springframework.stereotype.Service

@Service
class UrlService(
  private val urlRepository: UrlRepository,
  private val encodingService: EncodingService,
) {

  fun getUrl(shortCode: String): Url {
    return urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
  }

  fun createUrl(longUrl: String): Url {
    return urlRepository.save(Url(encodingService.encode(longUrl), longUrl))
  }

  fun updateUrl(shortCode: String, longUrl: String): Url {
    val existingUrl =
      urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
    return urlRepository.save(existingUrl.apply { this.longUrl = longUrl })
  }

  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }
}
