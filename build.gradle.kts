import com.google.protobuf.gradle.id

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "7.2.1"
	id("com.google.protobuf") version "0.9.5"
}

group = "com.behlers"
version = "0.0.1-SNAPSHOT"
description = "shortener"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

spotless {
	kotlin {
		ktfmt().googleStyle()
		target("**/*.kt")
		targetExclude("build/generated")
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven")
	}
}

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springframework:spring-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.zaxxer:HikariCP")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("com.google.protobuf:protobuf-kotlin:4.32.0")
	implementation("io.confluent:kafka-protobuf-serializer:8.0.0")
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.12")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:db2")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.kotest:kotest-assertions-core-jvm:5.7.2")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:4.32.0"
	}
	generateProtoTasks {
		all().forEach { task ->
			task.builtins {
				id("kotlin")
			}
		}
	}
}

sourceSets {
	main {
		kotlin {
			srcDirs("build/generated/source/proto/main/java")
			srcDirs("build/generated/source/proto/main/kotlin")
		}
	}
}

dependencyManagement {
	imports {
		mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.19.0")
	}
}