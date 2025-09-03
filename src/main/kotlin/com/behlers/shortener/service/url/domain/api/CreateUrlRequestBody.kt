package com.behlers.shortener.service.url.domain.api

/**
 * Request body for creating a shortened URL.
 *
 * @property longUrl The original URL to be shortened.
 */
class CreateUrlRequestBody(val longUrl: String)
