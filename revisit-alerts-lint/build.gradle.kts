plugins {
  id("java-library")
  id("kotlin")
  id("com.android.lint")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  compileOnly(libs.kotlin.stdlib)
  compileOnly(libs.lint.api)
  compileOnly(libs.lint.checks)

  testImplementation(libs.junit4)
  testImplementation(libs.lint.core)
  testImplementation(libs.lint.tests)
}
