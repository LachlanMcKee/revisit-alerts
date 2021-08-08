plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()

    applicationId = "com.lachlanmckee.revisit.android.app"
    versionCode = 1
    versionName = "1.0"
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(projects.revisitAlerts)
  implementation(libs.kotlin.stdlib)
  implementation(libs.androidx.appcompat)
}
