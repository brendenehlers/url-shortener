package com.behlers.shortener.service.analytics.controller

import com.behlers.shortener.service.analytics.domain.UrlStatsEntity
import com.behlers.shortener.service.analytics.service.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/statistics")
class StatisticsController(private val statisticsService: StatisticsService) {

  @GetMapping("/mostHits")
  fun getMostHits(): List<UrlStatsEntity> {
    return statisticsService.getSortedHits(StatisticsService.SortingOrder.DESC)
  }

  @GetMapping("/leastHits")
  fun getLeastHits(): List<UrlStatsEntity> {
    return statisticsService.getSortedHits(StatisticsService.SortingOrder.ASC)
  }
}
