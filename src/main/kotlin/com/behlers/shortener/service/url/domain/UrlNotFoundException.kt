package com.behlers.shortener.service.url.domain

class UrlNotFoundException(val shortCode: String) :
  Exception("URL with code '$shortCode' could not be found")
