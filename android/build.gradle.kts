plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cookierue.webview"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            manifestFile("src/main/AndroidManifest.xml")
            java.srcDirs("src/main/java")
            res.srcDirs("src/main/res")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.browser:browser:1.8.0")
}
