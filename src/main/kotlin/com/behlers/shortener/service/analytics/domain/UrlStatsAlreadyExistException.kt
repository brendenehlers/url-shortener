package com.behlers.shortener.service.analytics.domain

/**
 * Exception thrown when URL statistics already exist for a given short code.
 *
 * @param shortCode the short URL code for which statistics already exist
 */
class UrlStatsAlreadyExistException(shortCode: String) :
  Exception("URL statistics already exist for short code: $shortCode") {}
