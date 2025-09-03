package com.behlers.shortener.service.analytics.service

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.repository.UrlStatsRepository
import org.springframework.stereotype.Service

/** Service for retrieving URL statistics. */
@Service
class StatisticsService(private val urlStatsRepository: UrlStatsRepository) {
  /**
   * Retrieves URL statistics sorted by hit count.
   *
   * @param sortingOrder the order to sort (ascending or descending)
   * @param limit maximum number of results (default: 10)
   * @return list of UrlStatsEntity sorted by hit count
   */
  fun getSortedHits(sortingOrder: SortingOrder, limit: Int = 10): List<UrlStatsEntity> {
    return when (sortingOrder) {
      SortingOrder.DESC -> urlStatsRepository.getMostHitWithLimit(limit)
      SortingOrder.ASC -> urlStatsRepository.getLeastHitWithLimit(limit)
    }
  }

  /** Sorting order for URL statistics. */
  enum class SortingOrder {
    ASC,
    DESC,
  }
}
