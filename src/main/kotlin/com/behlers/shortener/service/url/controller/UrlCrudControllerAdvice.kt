package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.shared.domain.DefaultErrorResponse
import com.behlers.shortener.service.shared.domain.UrlNotFoundException
import com.behlers.shortener.service.url.domain.InvalidCodeException
import com.behlers.shortener.service.url.domain.InvalidUrlSyntaxException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Handles exceptions for URL CRUD operations, providing standardized error responses.
 */
@RestControllerAdvice(basePackageClasses = [UrlCrudController::class])
class UrlCrudControllerAdvice {

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

  /**
   * Handles InvalidUrlSyntaxException and InvalidCodeException, returning a BAD_REQUEST error response.
   * @param exception The thrown exception (invalid URL syntax or code).
   * @return ResponseEntity containing the error details.
   */
  @ExceptionHandler(InvalidUrlSyntaxException::class, InvalidCodeException::class)
  fun handleInvalidUrlSyntaxException(exception: Exception): ResponseEntity<DefaultErrorResponse> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(DefaultErrorResponse(reason = exception.message ?: "Bad input in request"))
  }
}
