package com.behlers.shortener.service.analytics.service

import com.behlers.shortener.service.analytics.domain.UrlStatsAlreadyExistException
import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.domain.UrlStatsNotFoundException
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class UrlStatsService(private val urlStatsRepository: UrlStatsRepository) {

  fun updateUrlStats(shortCode: String): UrlStatsEntity {
    val stats =
      urlStatsRepository.findByShortCode(shortCode) ?: throw UrlStatsNotFoundException(shortCode)

    return urlStatsRepository.save(UrlStatsEntity(stats.shortCode, stats.hits + 1, Instant.now()))
  }

  fun createUrlStats(shortCode: String): UrlStatsEntity {
    if (urlStatsRepository.existsByShortCode(shortCode))
      throw UrlStatsAlreadyExistException(shortCode)

    return urlStatsRepository.save(UrlStatsEntity(shortCode, 0, null))
  }
}
