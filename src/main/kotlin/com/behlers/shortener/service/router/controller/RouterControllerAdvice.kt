package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.shared.domain.DefaultErrorResponse
import com.behlers.shortener.service.shared.domain.UrlNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Handles exceptions for router-related controllers, providing standardized error responses.
 */
@RestControllerAdvice
class RouterControllerAdvice {

  /**
   * Handles UrlNotFoundException and returns a NOT_FOUND error response.
   * @param exception The thrown UrlNotFoundException.
   * @return ResponseEntity containing the error details.
   */
  @ExceptionHandler(UrlNotFoundException::class)
  fun handleUrlNotFoundException(
    exception: UrlNotFoundException
  ): ResponseEntity<DefaultErrorResponse> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(DefaultErrorResponse(reason = exception.message ?: "Unknown URL not found error"))
  }
}
