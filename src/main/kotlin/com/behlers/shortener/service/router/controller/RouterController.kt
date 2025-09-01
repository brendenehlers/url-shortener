package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.router.service.RouterService
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import com.behlers.shortener.service.shared.domain.urlAnalyticsMessage
import com.behlers.shortener.service.shared.service.MessagingService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for handling routing requests from short codes to long URLs.
 * Sends analytics messages and performs HTTP redirects.
 * @property routerService Service for URL resolution.
 * @property messagingService Service for sending analytics messages.
 */
@RestController
@RequestMapping("/")
class RouterController(
  private val routerService: RouterService,
  private val messagingService: MessagingService,
) {
  /**
   * Resolves the short code, sends an analytics message, and redirects to the long URL.
   * @param response HTTP response for sending the redirect.
   * @param shortCode The short code to resolve.
   * @throws com.behlers.shortener.service.shared.domain.UrlNotFoundException if the short code does not exist.
   * @see RouterService.getUrl
   */
  @GetMapping("{shortCode}")
  fun route(response: HttpServletResponse, @PathVariable shortCode: String) {
    val longUrl = routerService.getUrl(shortCode)
    messagingService.sendAnalyticsMessage(
      urlAnalyticsMessage {
        this.shortCode = shortCode
        type = UrlAnalyticsMessageType.Update
      }
    )
    response.sendRedirect(longUrl)
  }
}
