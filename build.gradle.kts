plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    `maven-publish`
}

group = "com.sharazan"
version = "1.0-SNAPSHOT"

val gitVersion: String = try {
    providers.exec {
        commandLine("git", "describe", "--tags", "--abbrev=0")
    }.standardOutput.asText.get().trim()
} catch (e: Exception) {
    "0.0.0-dev"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    api("io.insert-koin:koin-core:4.2.0-RC1")
    api("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.11.0")

    api("org.http4k:http4k-core:6.31.1.0")

    implementation("com.github.37hulk37:sharazan-logging:1.0.0")

    testImplementation("io.insert-koin:koin-test:4.2.0-RC1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

publishing {
    publications {
        create<MavenPublication>("publish") {
            from(components["java"])
            groupId = "com.github.37hulk37"
            artifactId = "sharazan-${project.name}"
            version = gitVersion
        }
    }

    repositories {
        mavenLocal()
    }
}

tasks.test {
    useJUnitPlatform()
}