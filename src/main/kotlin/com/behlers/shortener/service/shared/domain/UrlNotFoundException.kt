package com.behlers.shortener.service.shared.domain

class UrlNotFoundException(val shortCode: String) :
  Exception("URL with code '$shortCode' could not be found")
