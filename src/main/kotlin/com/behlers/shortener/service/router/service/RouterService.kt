package com.behlers.shortener.service.router.service

import com.behlers.shortener.service.shared.service.UrlService
import org.springframework.stereotype.Service

@Service
class RouterService(private val urlService: UrlService) {
  fun getUrl(shortCode: String): String {
    return urlService.getUrl(shortCode).longUrl
  }
}
