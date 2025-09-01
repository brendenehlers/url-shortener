package com.behlers.shortener.service.analytics.controller

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.service.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for retrieving URL statistics.
 */
@RestController
@RequestMapping("/api/v1/statistics")
class StatisticsController(private val statisticsService: StatisticsService) {

  /**
   * Retrieves URLs with the most hits, sorted in descending order.
   *
   * @return list of UrlStatsEntity sorted by hit count (descending)
   */
  @GetMapping("/mostHits")
  fun getMostHits(): List<UrlStatsEntity> {
    return statisticsService.getSortedHits(StatisticsService.SortingOrder.DESC)
  }

  /**
   * Retrieves URLs with the least hits, sorted in ascending order.
   *
   * @return list of UrlStatsEntity sorted by hit count (ascending)
   */
  @GetMapping("/leastHits")
  fun getLeastHits(): List<UrlStatsEntity> {
    return statisticsService.getSortedHits(StatisticsService.SortingOrder.ASC)
  }
}
