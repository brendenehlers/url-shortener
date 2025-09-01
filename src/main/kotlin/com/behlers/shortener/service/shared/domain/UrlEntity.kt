package com.behlers.shortener.service.shared.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

/**
 * JPA entity representing a shortened URL.
 *
 * @property shortCode Unique short code for the URL.
 * @property longUrl Original long URL.
 * @property createdAt Timestamp when the URL was created.
 * @property updatedAt Timestamp when the URL was last updated.
 */
@Entity(name = "urls")
class UrlEntity(
  @Id @Column(name = "short_code") var shortCode: String,
  @Column(name = "long_url") var longUrl: String,
  @Column(name = "created_at") var createdAt: Instant = Instant.now(),
  @Column(name = "updated_at") var updatedAt: Instant = Instant.now(),
)
