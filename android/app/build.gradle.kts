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

    // Release signing is injected at build time from CI-provided env vars
    // (decoded from the APK_SIGNING_KEY GitHub secret). Debug builds are unaffected.
    signingConfigs {
        create("release") {
            storeFile = System.getenv("SIGNING_STORE_FILE")?.let { File(it) }
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            // Sign only when the keystore env vars are present; otherwise AGP
            // produces an unsigned release APK (no failure) so other workflows keep working.
            if (System.getenv("SIGNING_STORE_FILE") != null) {
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
}
