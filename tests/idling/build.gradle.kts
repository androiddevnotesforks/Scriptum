plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "sgtmelon.test.idling"

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

    implementation(libs.android.transition)
    implementation(libs.test.espresso.idling) // TODO make only for androidTestImplementation
    implementation(libs.timber)
}