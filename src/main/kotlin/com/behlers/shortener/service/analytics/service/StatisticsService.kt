package com.behlers.shortener.service.analytics.service

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import org.springframework.stereotype.Service

@Service
class StatisticsService(private val urlStatsRepository: UrlStatsRepository) {
  fun getSortedHits(sortingOrder: SortingOrder, limit: Int = 10): List<UrlStatsEntity> {
    return when (sortingOrder) {
      SortingOrder.DESC -> urlStatsRepository.getMostHitWithLimit(limit)
      SortingOrder.ASC -> urlStatsRepository.getLeastHitWithLimit(limit)
    }
  }

  enum class SortingOrder {
    ASC,
    DESC,
  }
}
