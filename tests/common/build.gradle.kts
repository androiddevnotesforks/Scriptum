plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "sgtmelon.test.common"

    compileSdk = AndroidVersion.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidVersion.MIN_SDK
        targetSdk = AndroidVersion.TARGET_SDK

        consumerProguardFiles(ProFiles.CONSUMER)
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
    implementation(project(":libraries:extensions"))

    implementation(libs.test.core)
    implementation(libs.test.junit)
}