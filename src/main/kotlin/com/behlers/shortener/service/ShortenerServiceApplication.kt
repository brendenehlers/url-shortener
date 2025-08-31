package com.behlers.shortener.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication @EnableJpaRepositories @EnableKafka class ShortenerServiceApplication

fun main(args: Array<String>) {
  runApplication<ShortenerServiceApplication>(*args)
}
