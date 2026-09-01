import java.io.File

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 35

    namespace = "com.whiskful.webview"

    defaultConfig {
        applicationId = "com.whiskful.webview"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "alpha-1.0.01"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        release {
            // Sign only when all keystore env vars are present.
            // When absent, AGP produces an unsigned APK (no failure).
            val storeFile = System.getenv("SIGNING_STORE_FILE")
            val storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            val keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            val keyPassword = System.getenv("SIGNING_KEY_PASSWORD")

            if (storeFile != null && storePassword != null && keyAlias != null && keyPassword != null) {
                signingConfigs {
                    create("release") {
                        storeFile = File(storeFile)
                        storePassword = storePassword
                        storeType = "pkcs12"
                        keyAlias = keyAlias
                        keyPassword = keyPassword
                    }
                }
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            isDebuggable = false
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
