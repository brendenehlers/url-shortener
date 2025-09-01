package com.behlers.shortener.service.analytics.domain

/**
 * Exception thrown when URL statistics are not found for a given short code.
 *
 * @param shortCode the short URL code for which statistics are missing
 */
class UrlStatsNotFoundException(shortCode: String) :
  Exception("URL statistics not found with short code: $shortCode")
