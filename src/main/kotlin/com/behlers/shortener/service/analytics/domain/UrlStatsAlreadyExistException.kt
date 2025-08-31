package com.behlers.shortener.service.analytics.domain

class UrlStatsAlreadyExistException(shortCode: String) :
  Exception("URL statistics already exist for short code: $shortCode") {}
