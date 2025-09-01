package com.behlers.shortener.service.router.controller

import com.behlers.shortener.service.shared.domain.DefaultErrorResponse
import com.behlers.shortener.service.shared.domain.UrlNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RouterControllerAdvice {

  @ExceptionHandler(UrlNotFoundException::class)
  fun handleUrlNotFoundException(
    exception: UrlNotFoundException
  ): ResponseEntity<DefaultErrorResponse> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(DefaultErrorResponse(reason = exception.message ?: "Unknown URL not found error"))
  }
}
