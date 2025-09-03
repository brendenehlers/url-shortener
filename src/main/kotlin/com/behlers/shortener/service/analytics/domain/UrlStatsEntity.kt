package com.behlers.shortener.service.analytics.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

/**
 * Entity representing URL statistics. Stores short code, hit count, and last hit timestamp.
 *
 * @property shortCode the short URL code (primary key)
 * @property hits the number of times the URL was accessed
 * @property lastHit the timestamp of the last access, or null if never accessed
 */
@Entity(name = "url_stats")
class UrlStatsEntity(
  @Id @Column(name = "short_code") val shortCode: String,
  @Column(name = "hits") val hits: Int = 0,
  @Column(name = "last_hit") val lastHit: Instant? = null,
)
