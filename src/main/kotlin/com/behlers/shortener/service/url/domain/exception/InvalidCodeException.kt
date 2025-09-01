package com.behlers.shortener.service.url.domain.exception

/**
 * Exception thrown when an invalid short code is provided.
 * @param shortCode The invalid short code.
 * @param cause The underlying cause of the failure, if any.
 */
class InvalidCodeException(shortCode: String, override val cause: Throwable?) :
  Exception("Invalid code: '$shortCode'")
