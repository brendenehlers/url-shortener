package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.router.service.RouterService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RouterController(private val routerService: RouterService) {
  @GetMapping("{shortCode}")
  fun route(response: HttpServletResponse, @PathVariable shortCode: String) {
    val longUrl = routerService.getUrl(shortCode)
    response.sendRedirect(longUrl)
  }
}
