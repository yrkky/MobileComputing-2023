plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.yrkky.mobilecomp"
    compileSdk = sdk.compile

    defaultConfig {
        applicationId = "com.yrkky.mobilecomp"

        minSdk = sdk.min
        targetSdk = sdk.target
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(androidx.core.ktx)
    implementation(androidx.compose.ui.ui)
    implementation(androidx.compose.material)
    implementation(androidx.compose.ui.preview)
    implementation(androidx.lifecycle.runtime_ktx)
    implementation(androidx.activity.activity_compose)

    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.core)
    androidTestImplementation(androidx.compose.ui.ui_test_junit)
    debugImplementation(androidx.compose.ui.ui_tooling)
    debugImplementation(androidx.compose.ui.ui_test_manifest)

    // Accompanist
    implementation(google.accompanist.insets)
    // Navigation
    implementation(androidx.navigation.compose)
    // ViewModel
    implementation(androidx.lifecycle.compose)
    // Coroutines
    implementation(kotlinx.coroutines.android.android)
    // ConstraintLayout
    implementation(androidx.constraintlayout.compose)
    // Foundation
    implementation(androidx.compose.foundation.foundation)
}