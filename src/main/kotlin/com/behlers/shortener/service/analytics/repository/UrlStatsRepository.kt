package com.behlers.shortener.service.analytics.repository

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.shared.domain.UrlEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

/**
 * Repository for accessing URL statistics entities.
 * Provides methods for finding, checking existence, and retrieving most/least hit URLs.
 */
interface UrlStatsRepository : CrudRepository<UrlStatsEntity, UrlEntity> {
  /**
   * Finds URL statistics by short code.
   *
   * @param shortCode the short URL code
   * @return UrlStatsEntity if found, null otherwise
   */
  fun findByShortCode(shortCode: String): UrlStatsEntity?

  /**
   * Checks if URL statistics exist for the given short code.
   *
   * @param shortCode the short URL code
   * @return true if statistics exist, false otherwise
   */
  fun existsByShortCode(shortCode: String): Boolean

  /**
   * Retrieves URLs with the most hits, limited by the specified count.
   *
   * @param limit maximum number of results
   * @return list of UrlStatsEntity sorted by hit count (descending)
   */
  @Query("select u from url_stats u order by u.hits desc limit :limit")
  fun getMostHitWithLimit(limit: Int): List<UrlStatsEntity>

  /**
   * Retrieves URLs with the least hits, limited by the specified count.
   *
   * @param limit maximum number of results
   * @return list of UrlStatsEntity sorted by hit count (ascending)
   */
  @Query("select u from url_stats u order by u.hits asc limit :limit")
  fun getLeastHitWithLimit(limit: Int): List<UrlStatsEntity>
}
