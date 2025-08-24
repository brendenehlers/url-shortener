package com.behlers.shortener.service.url.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "urls")
class Url(

  @Id
  @Column(name = "short_code")
  var shortCode: String,

  @Column(name = "long_url")
  var longUrl: String,

  @Column(name = "created_at")
  var createdAt: Instant = Instant.now(),

  @Column(name = "updated_at")
  var updatedAt: Instant = Instant.now(),
) {
}