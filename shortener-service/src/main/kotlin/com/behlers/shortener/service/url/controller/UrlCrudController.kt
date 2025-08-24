package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.UpdateUrlRequestBody
import com.behlers.shortener.service.url.domain.Url
import com.behlers.shortener.service.url.service.UrlService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/url")
class UrlCrudController(private val urlService: UrlService) {

  @GetMapping("/{shortCode}")
  fun getUrl(@PathVariable shortCode: String): Url? {
    return urlService.getUrl(shortCode)
  }

  @PostMapping
  fun createUrl(@RequestBody createUrlRequestBody: CreateUrlRequestBody): Url {
    return urlService.createUrl(createUrlRequestBody.longUrl)
  }

  @PostMapping("/{shortCode}")
  fun updateUrl(
    @PathVariable shortCode: String,
    @RequestBody updateUrlRequestBody: UpdateUrlRequestBody,
  ): Url {
    return urlService.updateUrl(shortCode, updateUrlRequestBody.longUrl)
  }

  @DeleteMapping("/{shortCode}")
  fun deleteUrl(@PathVariable shortCode: String) {
    urlService.deleteUrl(shortCode)
  }
}
