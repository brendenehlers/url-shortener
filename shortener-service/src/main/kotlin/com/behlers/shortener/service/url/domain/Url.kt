package com.behlers.shortener.service.url.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("urls")
class Url(

  @Id
  @Column("short_code")
  val shortCode: String,

  @Column("long_url")
  val longUrl: String,

  @Column("created_at")
  val createdAt: Instant = Instant.now(),

  @Column("updated_at")
  val updatedAt: Instant = Instant.now(),
)