import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
  repositories {
    mavenCentral()
    google()
  }
  dependencies {
    classpath(libs.plugin.androidTools)
    classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
    classpath(libs.plugin.mavenPublish)
    classpath(libs.plugin.dokka)
  }
}

plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.dependencyUpdates)
}

spotless {
  format("misc") {
    target("*.md", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
  kotlin {
    ktlint("0.39.0").userData(
      mapOf(
        "indent_size" to "2",
        "disabled_rules" to "no-wildcard-imports"
      )
    )
    target("**/*.kt")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**", "**/GeneratedLibraries.kt")
  }
  kotlinGradle {
    ktlint("0.39.0").userData(mapOf("indent_size" to "2"))
    target("**/*.gradle.kts")
    trimTrailingWhitespace()
    endWithNewline()
    targetExclude("**/build/**")
  }
}

fun isStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java) {
  rejectVersionIf {
    if (isStable(currentVersion)) {
      !isStable(candidate.version)
    } else {
      false
    }
  }
}

allprojects {
  repositories {
    mavenCentral()
    google()
  }
}
