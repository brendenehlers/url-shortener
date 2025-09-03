package com.behlers.shortener.service.url.domain.exception

/**
 * Exception thrown when a URL cannot be shortened due to invalid syntax.
 *
 * @param invalidUrl The URL that failed validation.
 * @param cause The underlying cause of the failure, if any.
 */
class InvalidUrlSyntaxException(invalidUrl: String, override val cause: Throwable? = null) :
  Exception("Could not shorten URL: '$invalidUrl'")
