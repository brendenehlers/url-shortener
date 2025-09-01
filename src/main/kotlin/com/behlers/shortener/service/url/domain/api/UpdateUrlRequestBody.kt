package com.behlers.shortener.service.url.domain.api

/**
 * Request body for updating a shortened URL.
 * @property longUrl The new URL to associate with the short code.
 */
class UpdateUrlRequestBody(val longUrl: String)
