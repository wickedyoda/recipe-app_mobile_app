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

    // Signing config: same key for debug and release so users can upgrade
    // without uninstalling (Android requires matching signatures for upgrades).
    val ksStoreFile = System.getenv("SIGNING_STORE_FILE") as String?
    val ksStorePassword = System.getenv("SIGNING_STORE_PASSWORD") as String?
    val ksKeyAlias = System.getenv("SIGNING_KEY_ALIAS") as String?
    val ksKeyPassword = System.getenv("SIGNING_KEY_PASSWORD") as String?

    if (ksStoreFile != null && ksStorePassword != null && ksKeyAlias != null && ksKeyPassword != null
        && File(ksStoreFile).exists()) {
        signingConfigs {
            create("release") {
                storeFile = File(ksStoreFile)
                storePassword = ksStorePassword
                storeType = "pkcs12"
                keyAlias = ksKeyAlias
                keyPassword = ksKeyPassword
            }
            create("debug") {
                storeFile = File(ksStoreFile)
                storePassword = ksStorePassword
                storeType = "pkcs12"
                keyAlias = ksKeyAlias
                keyPassword = ksKeyPassword
            }
        }
    }

    buildTypes {
        release {
            if (ksStoreFile != null && ksStorePassword != null && ksKeyAlias != null && ksKeyPassword != null
                && File(ksStoreFile).exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            isDebuggable = false
        }
        debug {
            if (ksStoreFile != null && ksStorePassword != null && ksKeyAlias != null && ksKeyPassword != null
                && File(ksStoreFile).exists()) {
                signingConfig = signingConfigs.getByName("debug")
            }
            isDebuggable = true
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
