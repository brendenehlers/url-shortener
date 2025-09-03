package com.behlers.shortener.service.router.service

import com.behlers.shortener.service.shared.service.UrlService
import org.springframework.stereotype.Service

/**
 * Service for resolving long URLs from short codes using UrlService.
 *
 * @property urlService Service for URL operations.
 */
@Service
class RouterService(private val urlService: UrlService) {
  /**
   * Retrieves the long URL associated with the given short code.
   *
   * @param shortCode The short code to resolve.
   * @return The corresponding long URL.
   * @throws com.behlers.shortener.service.shared.domain.UrlNotFoundException if the short code does
   *   not exist.
   */
  fun getUrl(shortCode: String): String {
    return urlService.getUrl(shortCode).longUrl
  }
}
