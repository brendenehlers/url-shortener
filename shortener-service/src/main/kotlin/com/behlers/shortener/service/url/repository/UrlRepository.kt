package com.behlers.shortener.service.url.repository

import com.behlers.shortener.service.url.domain.Url
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<Url, String> {
  fun getUrlByShortCode(shortCode: String): Url?

  fun deleteUrlByShortCode(shortCode: String)
}
