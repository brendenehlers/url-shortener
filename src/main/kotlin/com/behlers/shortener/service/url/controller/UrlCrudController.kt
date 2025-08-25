package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.DeleteUrlResponseBody
import com.behlers.shortener.service.url.domain.InvalidCodeException
import com.behlers.shortener.service.url.domain.InvalidUrlSyntaxException
import com.behlers.shortener.service.url.domain.UpdateUrlRequestBody
import com.behlers.shortener.service.url.domain.UrlEntity
import com.behlers.shortener.service.url.service.EncodingService
import com.behlers.shortener.service.url.service.UrlService
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/url")
class UrlCrudController(
  private val urlService: UrlService,
  private val encodingService: EncodingService,
) {

  @GetMapping("/{shortCode}")
  fun getUrl(@PathVariable shortCode: String): UrlEntity {
    if (!encodingService.isValidEncoding(shortCode)) throw InvalidCodeException(shortCode, null)
    return urlService.getUrl(shortCode)
  }

  @PostMapping
  fun createUrl(@RequestBody createUrlRequestBody: CreateUrlRequestBody): UrlEntity {
    val exceptionFn = { url: String, cause: Throwable ->
      throw InvalidUrlSyntaxException(url, cause)
    }
    try {
      val url = URI(createUrlRequestBody.longUrl).toURL()
      return urlService.createUrl(url)
    } catch (e: URISyntaxException) {
      exceptionFn(createUrlRequestBody.longUrl, e)
    } catch (e: MalformedURLException) {
      exceptionFn(createUrlRequestBody.longUrl, e)
    } catch (e: IllegalArgumentException) {
      exceptionFn(createUrlRequestBody.longUrl, e)
    }
  }

  @PostMapping("/{shortCode}")
  fun updateUrl(
    @PathVariable shortCode: String,
    @RequestBody updateUrlRequestBody: UpdateUrlRequestBody,
  ): UrlEntity {
    if (!encodingService.isValidEncoding(shortCode)) throw InvalidCodeException(shortCode, null)
    return urlService.updateUrl(shortCode, updateUrlRequestBody.longUrl)
  }

  @DeleteMapping("/{shortCode}")
  fun deleteUrl(@PathVariable shortCode: String): ResponseEntity<DeleteUrlResponseBody> {
    if (!encodingService.isValidEncoding(shortCode)) throw InvalidCodeException(shortCode, null)
    urlService.deleteUrl(shortCode)
    return ResponseEntity.status(HttpStatus.OK).body(DeleteUrlResponseBody("success"))
  }
}
