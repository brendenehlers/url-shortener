package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import com.behlers.shortener.service.shared.domain.UrlEntity
import com.behlers.shortener.service.shared.domain.urlAnalyticsMessage
import com.behlers.shortener.service.shared.service.EncodingService
import com.behlers.shortener.service.shared.service.MessagingService
import com.behlers.shortener.service.shared.service.UrlService
import com.behlers.shortener.service.url.domain.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.DeleteUrlResponseBody
import com.behlers.shortener.service.url.domain.InvalidCodeException
import com.behlers.shortener.service.url.domain.InvalidUrlSyntaxException
import com.behlers.shortener.service.url.domain.UpdateUrlRequestBody
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
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
  private val messagingService: MessagingService,
) {

  @GetMapping("/{shortCode}")
  fun getUrl(@PathVariable shortCode: String): UrlEntity {
    validateShortCode(shortCode)
    return urlService.getUrl(shortCode)
  }

  @PostMapping
  fun createUrl(@RequestBody createUrlRequestBody: CreateUrlRequestBody): UrlEntity {
    val url = createUrlWrapper(createUrlRequestBody.longUrl) { urlService.createUrl(it) }
    messagingService.sendAnalyticsMessage(
      urlAnalyticsMessage {
        shortCode = url.shortCode
        type = UrlAnalyticsMessageType.Create
      }
    )
    return url
  }

  @PostMapping("/{shortCode}")
  fun updateUrl(
    @PathVariable shortCode: String,
    @RequestBody updateUrlRequestBody: UpdateUrlRequestBody,
  ): UrlEntity {
    validateShortCode(shortCode)
    return createUrlWrapper(updateUrlRequestBody.longUrl) { urlService.updateUrl(shortCode, it) }
  }

  @DeleteMapping("/{shortCode}")
  fun deleteUrl(@PathVariable shortCode: String): ResponseEntity<DeleteUrlResponseBody> {
    validateShortCode(shortCode)
    urlService.deleteUrl(shortCode)
    return ResponseEntity.status(HttpStatus.OK).body(DeleteUrlResponseBody("success"))
  }

  private fun validateShortCode(shortCode: String) {
    if (!encodingService.isValidEncoding(shortCode)) throw InvalidCodeException(shortCode, null)
  }

  private fun createUrlWrapper(longUrl: String, fn: (URL) -> UrlEntity): UrlEntity {
    val exceptionFn = { url: String, cause: Throwable ->
      throw InvalidUrlSyntaxException(url, cause)
    }
    try {
      val url = URI(longUrl).toURL()
      return fn(url)
    } catch (e: URISyntaxException) {
      exceptionFn(longUrl, e)
    } catch (e: MalformedURLException) {
      exceptionFn(longUrl, e)
    } catch (e: IllegalArgumentException) {
      exceptionFn(longUrl, e)
    }
  }
}
