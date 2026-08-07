plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 35

    namespace = "com.wiskfful.webview"

    defaultConfig {
        applicationId = "com.wiskfful.webview"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
