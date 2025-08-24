package com.behlers.shortener.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication @EnableJpaRepositories class ShortenerServiceApplication

fun main(args: Array<String>) {
  runApplication<ShortenerServiceApplication>(*args)
}
