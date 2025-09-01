package com.behlers.shortener.service.shared.repository

import com.behlers.shortener.service.shared.domain.UrlEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

@Transactional
interface UrlRepository : JpaRepository<UrlEntity, String> {
  fun getUrlByShortCode(shortCode: String): UrlEntity?

  fun deleteUrlByShortCode(shortCode: String)

  fun existsUrlByShortCode(shortCode: String): Boolean
}
