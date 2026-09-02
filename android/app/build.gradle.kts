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
            val ksStoreFile = System.getenv("SIGNING_STORE_FILE")
            val ksStorePassword = System.getenv("SIGNING_STORE_PASSWORD")
            val ksKeyAlias = System.getenv("SIGNING_KEY_ALIAS")
            val ksKeyPassword = System.getenv("SIGNING_KEY_PASSWORD")

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
}