package com.behlers.shortener.service.analytics.repository

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.shared.domain.UrlEntity
import org.springframework.data.repository.CrudRepository

interface UrlStatsRepository : CrudRepository<UrlStatsEntity, UrlEntity> {
  fun findByShortCode(shortCode: String): UrlStatsEntity?

  fun existsByShortCode(shortCode: String): Boolean
}
