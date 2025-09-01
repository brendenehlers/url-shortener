package com.behlers.shortener.service.shared.service

import com.behlers.shortener.service.shared.domain.UrlEntity
import com.behlers.shortener.service.shared.domain.UrlNotFoundException
import com.behlers.shortener.service.shared.repository.UrlRepository
import java.net.URL
import org.springframework.stereotype.Service

/**
 * Service for managing shortened URLs.
 *
 * Handles creation, retrieval, update, and deletion of URL entities.
 *
 * @property urlRepository Repository for URL entities.
 * @property encodingService Service for encoding short codes.
 */
@Service
class UrlService(
  private val urlRepository: UrlRepository,
  private val encodingService: EncodingService,
) {

  /**
   * Retrieves a URL entity by its short code.
   *
   * @param shortCode The short code to look up.
   * @return The UrlEntity if found.
   * @throws UrlNotFoundException if the short code does not exist.
   */
  fun getUrl(shortCode: String): UrlEntity {
    return urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
  }

  /**
   * Creates a new shortened URL entity.
   *
   * If a collision occurs, retries with incremented salt until unique.
   *
   * @param longUrl The original long URL.
   * @return The created UrlEntity.
   */
  fun createUrl(longUrl: URL): UrlEntity {
    var shortCode = encodingService.encode(longUrl.toString())

    var i = 0
    while (urlRepository.existsUrlByShortCode(shortCode)) {
      shortCode = encodingService.encode(longUrl.toString(), (i++).toString())
    }

    return urlRepository.save(UrlEntity(shortCode, longUrl.toString()))
  }

  /**
   * Updates the long URL for an existing short code.
   *
   * @param shortCode The short code to update.
   * @param longUrl The new long URL.
   * @return The updated UrlEntity.
   * @throws UrlNotFoundException if the short code does not exist.
   */
  fun updateUrl(shortCode: String, longUrl: URL): UrlEntity {
    val existingUrl =
      urlRepository.getUrlByShortCode(shortCode) ?: throw UrlNotFoundException(shortCode)
    return urlRepository.save(existingUrl.apply { this.longUrl = longUrl.toString() })
  }

  /**
   * Deletes a URL entity by its short code.
   *
   * @param shortCode The short code to delete.
   */
  fun deleteUrl(shortCode: String) {
    urlRepository.deleteUrlByShortCode(shortCode)
  }
}
