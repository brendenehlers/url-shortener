package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.repository.UrlRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlService(
  val urlRepository: UrlRepository
) {

  fun getUrl(shortCode: String): Mono<Url> {
    return urlRepository.getUrlByShortCode(shortCode)
  }

  fun createUrl(longUrl: String): Mono<Url> {
    return urlRepository.save(
      Url(
        createShortCode(longUrl),
        longUrl,
    )
    )
  }

  fun updateUrl(shortCode: String, longUrl: String): Mono<Url> {
    return urlRepository.save(Url(
      shortCode,
      longUrl,
    ))
  }

  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }

  private fun createShortCode(longUrl: String): String {
    TODO("Not implemented")
  }
}