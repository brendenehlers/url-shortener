package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.router.service.RouterService
import com.behlers.shortener.service.shared.config.KafkaConfig
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessage
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import com.behlers.shortener.service.shared.domain.urlAnalyticsMessage
import com.behlers.shortener.service.shared.service.MessagingService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RouterController(
  private val routerService: RouterService,
  private val messagingService: MessagingService,
) {
  @GetMapping("{shortCode}")
  fun route(response: HttpServletResponse, @PathVariable shortCode: String) {
    val longUrl = routerService.getUrl(shortCode)
    messagingService.sendAnalyticsMessage(
      urlAnalyticsMessage {
        this.shortCode = shortCode
        type = UrlAnalyticsMessageType.Update
      },
    )
    response.sendRedirect(longUrl)
  }
}
