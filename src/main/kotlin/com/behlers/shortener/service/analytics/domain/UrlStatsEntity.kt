package com.behlers.shortener.service.analytics.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "url_stats")
class UrlStatsEntity(
  @Id @Column(name = "short_code") val shortCode: String,
  @Column(name = "hits") val hits: Int = 0,
  @Column(name = "last_hit") val lastHit: Instant? = null,
)
