package com.behlers.shortener.service.url.repository

import com.behlers.shortener.service.url.domain.Url
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

@Transactional
interface UrlRepository : JpaRepository<Url, String> {
  fun getUrlByShortCode(shortCode: String): Url?

  fun deleteUrlByShortCode(shortCode: String)
}
