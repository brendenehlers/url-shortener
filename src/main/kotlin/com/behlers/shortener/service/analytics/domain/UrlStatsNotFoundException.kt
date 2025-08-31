package com.behlers.shortener.service.analytics.domain

class UrlStatsNotFoundException(shortCode: String) :
  Exception("URL statistics not found with short code: $shortCode")
