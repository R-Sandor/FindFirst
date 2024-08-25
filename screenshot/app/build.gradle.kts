/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.9/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    kotlin("jvm") version "1.8.10" // Adjust the Kotlin version as needed
    kotlin("plugin.spring") version "1.8.10" // For Spring support
    id("org.springframework.boot") version "3.2.4" // Adjust the Spring Boot version as needed
    id("io.spring.dependency-management") version "1.1.0" // For dependency management
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.microsoft.playwright:playwright:1.41.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
	  annotationProcessor("org.projectlombok:lombok")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Define the main class for the application.
    mainClass = "dev.findfirst.screenshot.FindFirstScreenshot"
}
