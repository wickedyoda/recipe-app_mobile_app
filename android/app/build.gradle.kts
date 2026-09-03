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
    val ksStoreFile: String? = System.getenv("SIGNING_STORE_FILE")
    val ksStorePassword: String? = System.getenv("SIGNING_STORE_PASSWORD")
    val ksKeyAlias: String? = System.getenv("SIGNING_KEY_ALIAS")
    val ksKeyPassword: String? = System.getenv("SIGNING_KEY_PASSWORD")

    if (!ksStoreFile.isNullOrEmpty() && !ksStorePassword.isNullOrEmpty() &&
        !ksKeyAlias.isNullOrEmpty() && !ksKeyPassword.isNullOrEmpty() &&
        File(ksStoreFile).exists()) {
        signingConfigs {
            create("release") {
                storeFile = File(ksStoreFile)
                storePassword = ksStorePassword
                storeType = "pkcs12"
                keyAlias = ksKeyAlias
                keyPassword = ksKeyPassword
            }
            create("debugSigned") {
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
                signingConfig = signingConfigs.getByName("debugSigned")
            }
            isDebuggable = true
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
