package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.shared.domain.InvalidCodeException
import com.behlers.shortener.service.shared.domain.UrlAnalyticsMessageType
import com.behlers.shortener.service.shared.domain.UrlEntity
import com.behlers.shortener.service.shared.domain.urlAnalyticsMessage
import com.behlers.shortener.service.shared.service.EncodingService
import com.behlers.shortener.service.shared.service.MessagingService
import com.behlers.shortener.service.shared.service.UrlService
import com.behlers.shortener.service.url.domain.api.CreateUrlRequestBody
import com.behlers.shortener.service.url.domain.api.DeleteUrlResponseBody
import com.behlers.shortener.service.url.domain.api.UpdateUrlRequestBody
import com.behlers.shortener.service.url.domain.exception.InvalidUrlSyntaxException
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

/**
 * Controller for CRUD operations on shortened URLs. Provides endpoints for creating, retrieving,
 * updating, and deleting URLs. Validates input and sends analytics messages for relevant
 * operations.
 *
 * @property urlService Service for URL persistence and retrieval.
 * @property encodingService Service for short code validation.
 * @property messagingService Service for sending analytics messages.
 */
@RestController
@RequestMapping("/api/v1/url")
class UrlCrudController(
  private val urlService: UrlService,
  private val encodingService: EncodingService,
  private val messagingService: MessagingService,
) {

  /**
   * Retrieves the URL entity for the given short code.
   *
   * @param shortCode The short code to resolve.
   * @return The corresponding UrlEntity.
   * @throws InvalidCodeException if the short code is invalid.
   * @throws com.behlers.shortener.service.shared.domain.UrlNotFoundException if the short code does
   *   not exist.
   */
  @GetMapping("/{shortCode}")
  fun getUrl(@PathVariable shortCode: String): UrlEntity {
    validateShortCode(shortCode)
    return urlService.getUrl(shortCode)
  }

  /**
   * Creates a new shortened URL.
   *
   * @param createUrlRequestBody Request body containing the long URL.
   * @return The created UrlEntity.
   * @throws InvalidUrlSyntaxException if the long URL is invalid.
   */
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

  /**
   * Updates the long URL for an existing short code.
   *
   * @param shortCode The short code to update.
   * @param updateUrlRequestBody Request body containing the new long URL.
   * @return The updated UrlEntity.
   * @throws InvalidCodeException if the short code is invalid.
   * @throws InvalidUrlSyntaxException if the new long URL is invalid.
   * @throws com.behlers.shortener.service.shared.domain.UrlNotFoundException if the short code does
   *   not exist.
   */
  @PostMapping("/{shortCode}")
  fun updateUrl(
    @PathVariable shortCode: String,
    @RequestBody updateUrlRequestBody: UpdateUrlRequestBody,
  ): UrlEntity {
    validateShortCode(shortCode)
    return createUrlWrapper(updateUrlRequestBody.longUrl) { urlService.updateUrl(shortCode, it) }
  }

  /**
   * Deletes the URL associated with the given short code.
   *
   * @param shortCode The short code to delete.
   * @return ResponseEntity containing the deletion result message.
   * @throws InvalidCodeException if the short code is invalid.
   * @throws com.behlers.shortener.service.shared.domain.UrlNotFoundException if the short code does
   *   not exist.
   */
  @DeleteMapping("/{shortCode}")
  fun deleteUrl(@PathVariable shortCode: String): ResponseEntity<DeleteUrlResponseBody> {
    validateShortCode(shortCode)
    urlService.deleteUrl(shortCode)
    return ResponseEntity.status(HttpStatus.OK).body(DeleteUrlResponseBody("success"))
  }

  /**
   * Validates the short code format using the encoding service.
   *
   * @param shortCode The short code to validate.
   * @throws InvalidCodeException if the short code is invalid.
   */
  private fun validateShortCode(shortCode: String) {
    if (!encodingService.isValidEncoding(shortCode)) throw InvalidCodeException(shortCode, null)
  }

  /**
   * Wraps URL creation and update logic, handling syntax validation and exceptions.
   *
   * @param longUrl The long URL to validate and process.
   * @param fn Function to execute with the validated URL.
   * @return The resulting UrlEntity.
   * @throws InvalidUrlSyntaxException if the long URL is invalid.
   */
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
