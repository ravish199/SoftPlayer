plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ravish.softplayer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ravish.softplayer"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.volley)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.espresso.core)
    implementation(libs.material)
    implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout.compose) // Or the latest version

        implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose.android)
    implementation(project(":player"))
    implementation(libs.androidx.foundation.android)
    implementation(project(":soundeffects"))
    implementation(libs.androidx.datastore.core.android) // Check for the latest version

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

        // For ExoPlayer core
        implementation(libs.androidx.media3.exoplayer) // Check for the latest version

        // For MediaSession integration (recommended for proper system integration)
        implementation(libs.androidx.media3.session)
// build.gradle or build.gradle.kts
    implementation(libs.androidx.navigation.compose) // Check latest version
    implementation(libs.androidx.core.splashscreen)
        // For UI components if you need them (e.g., PlayerView, PlayerControlView)
        // implementation "androidx.media3:media3-ui:1.3.1"
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.datastore.preferences)



}