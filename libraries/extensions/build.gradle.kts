plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
}

android {
    namespace = "sgtmelon.extensions"

    compileSdk = AndroidVersion.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidVersion.MIN_SDK
        targetSdk = AndroidVersion.TARGET_SDK

        consumerProguardFiles(ProFiles.CONSUMER)

        buildConfigField(type = "String", name = "DATE_FORMAT_FULL", value = "\"yyyy-MM-dd HH:mm:ss\"")
        buildConfigField(type = "String", name = "DATE_FORMAT_SHORT", value = "\"d MMM\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile(ProFiles.DEFAULT), ProFiles.MAIN)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.kotlin.serialization)
    implementation(libs.android.core)
    implementation(libs.android.appcompat)
    implementation(libs.android.lifecycle.runtime)
}