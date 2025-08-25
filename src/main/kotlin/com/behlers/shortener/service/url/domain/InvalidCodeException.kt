package com.behlers.shortener.service.url.domain

class InvalidCodeException(shortCode: String, override val cause: Throwable?) :
  Exception("Invalid code: '$shortCode'")
