package com.behlers.shortener.service.shared.domain

/**
 * Exception thrown when a URL with the specified short code cannot be found.
 *
 * @param shortCode The short code that was not found.
 */
class UrlNotFoundException(val shortCode: String) :
  Exception("URL with code '$shortCode' could not be found")
