package com.behlers.shortener.service.shared.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/** Redirect root ("/") to the Swagger UI to avoid exposing the Whitelabel Error Page. */
@Controller
class IndexController {

  @GetMapping("/")
  fun index(): String {
    return "redirect:/swagger-ui.html"
  }
}
