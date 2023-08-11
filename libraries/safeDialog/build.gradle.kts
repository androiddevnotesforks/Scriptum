plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
}

android {
    namespace = "sgtmelon.safedialog"

    compileSdk = AndroidVersion.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidVersion.MIN_SDK
        targetSdk = AndroidVersion.TARGET_SDK

        consumerProguardFiles(ProFiles.CONSUMER)
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile(ProFiles.ANDROID_OPTIMIZE), ProFiles.MAIN)
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
    implementation(project(":tests:prod"))
    testImplementation(project(":tests:common"))
    testImplementation(project(":tests:uniter"))

    implementation(libs.kotlin.serialization)

    implementation(libs.android.appcompat)
    implementation(libs.android.material)
    implementation(libs.android.lifecycle.runtime)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
}