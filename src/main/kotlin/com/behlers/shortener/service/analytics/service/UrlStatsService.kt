package com.behlers.shortener.service.analytics.service

import com.behlers.shortener.service.analytics.domain.UrlStatsAlreadyExistException
import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.domain.UrlStatsNotFoundException
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import java.time.Instant
import org.springframework.stereotype.Service

/**
 * Service for managing URL statistics. Provides creation and update operations for URL statistics
 * entities.
 */
@Service
class UrlStatsService(private val urlStatsRepository: UrlStatsRepository) {

  /**
   * Updates URL statistics for the given short code by incrementing hit count and updating last hit
   * timestamp.
   *
   * @param shortCode the short URL code
   * @return updated UrlStatsEntity
   * @throws UrlStatsNotFoundException if statistics for the short code do not exist
   */
  fun updateUrlStats(shortCode: String): UrlStatsEntity {
    val stats =
      urlStatsRepository.findByShortCode(shortCode) ?: throw UrlStatsNotFoundException(shortCode)

    return urlStatsRepository.save(UrlStatsEntity(stats.shortCode, stats.hits + 1, Instant.now()))
  }

  /**
   * Creates URL statistics for the given short code.
   *
   * @param shortCode the short URL code
   * @return newly created UrlStatsEntity
   * @throws UrlStatsAlreadyExistException if statistics for the short code already exist
   */
  fun createUrlStats(shortCode: String): UrlStatsEntity {
    if (urlStatsRepository.existsByShortCode(shortCode))
      throw UrlStatsAlreadyExistException(shortCode)

    return urlStatsRepository.save(UrlStatsEntity(shortCode, 0, null))
  }
}
