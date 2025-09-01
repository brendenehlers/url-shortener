package com.behlers.shortener.service.analytics.repository

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.shared.domain.UrlEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UrlStatsRepository : CrudRepository<UrlStatsEntity, UrlEntity> {
  fun findByShortCode(shortCode: String): UrlStatsEntity?

  fun existsByShortCode(shortCode: String): Boolean

  @Query("select u from url_stats u order by u.hits desc limit :limit")
  fun getMostHitWithLimit(limit: Int): List<UrlStatsEntity>

  @Query("select u from url_stats u order by u.hits asc limit :limit")
  fun getLeastHitWithLimit(limit: Int): List<UrlStatsEntity>
}
