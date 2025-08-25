package com.behlers.shortener.service.url.domain

class InvalidUrlSyntaxException(invalidUrl: String, override val cause: Throwable? = null) :
  Exception("Could not shorten URL: '$invalidUrl'")
