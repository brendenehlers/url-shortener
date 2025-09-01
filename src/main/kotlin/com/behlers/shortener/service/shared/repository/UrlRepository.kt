package com.behlers.shortener.service.shared.repository

import com.behlers.shortener.service.shared.domain.UrlEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA repository for UrlEntity.
 *
 * Provides methods for retrieving, deleting, and checking existence of URLs by short code.
 */
@Transactional
interface UrlRepository : JpaRepository<UrlEntity, String> {
  /**
   * Retrieves a URL entity by its short code.
   *
   * @param shortCode The short code to look up.
   * @return The UrlEntity if found, or null.
   */
  fun getUrlByShortCode(shortCode: String): UrlEntity?

  /**
   * Deletes a URL entity by its short code.
   *
   * @param shortCode The short code to delete.
   */
  fun deleteUrlByShortCode(shortCode: String)

  /**
   * Checks if a URL entity exists for the given short code.
   *
   * @param shortCode The short code to check.
   * @return True if the URL exists, false otherwise.
   */
  fun existsUrlByShortCode(shortCode: String): Boolean
}
