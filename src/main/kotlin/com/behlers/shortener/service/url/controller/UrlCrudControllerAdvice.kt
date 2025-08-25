package com.behlers.shortener.service.url.controller

import com.behlers.shortener.service.shared.domain.DefaultErrorResponse
import com.behlers.shortener.service.url.domain.InvalidUrlSyntaxException
import com.behlers.shortener.service.url.domain.UrlNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [UrlCrudController::class])
class UrlCrudControllerAdvice {

  @ExceptionHandler(UrlNotFoundException::class)
  fun handleUrlNotFoundException(
    exception: UrlNotFoundException
  ): ResponseEntity<DefaultErrorResponse> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(DefaultErrorResponse(reason = exception.message ?: "Unknown URL not found error"))
  }

  @ExceptionHandler(InvalidUrlSyntaxException::class)
  fun handleInvalidUrlSyntaxException(exception: InvalidUrlSyntaxException): ResponseEntity<DefaultErrorResponse> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(DefaultErrorResponse(reason = exception.message ?: "Bad input in request"))
  }
}
