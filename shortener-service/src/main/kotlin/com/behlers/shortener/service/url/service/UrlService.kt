package com.behlers.shortener.service.url.service

import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.repository.UrlRepository
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UrlService(
  val urlRepository: UrlRepository,
  val r2dbcEntityTemplate: R2dbcEntityTemplate,
) {

  // TODO replace this with actual algorithm
  private var tempCode = "1"

  fun getUrl(shortCode: String): Mono<Url> {
    return urlRepository.getUrlByShortCode(shortCode)
  }

  fun createUrl(longUrl: String): Mono<Url> {
    return r2dbcEntityTemplate.insert(Url(
        createShortCode(longUrl),
        longUrl,
    ))
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
    val newCode = tempCode
    tempCode = (tempCode.toInt() + 1).toString()
    return newCode
  }
}