package com.behlers.shortener.service.url.repository

import com.behlers.shortener.service.url.domain.Url
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UrlRepository : ReactiveCrudRepository<Url, String> {
  fun getUrlByShortCode(shortCode: String): Mono<Url>
  fun deleteUrlByShortCode(shortCode: String)
}