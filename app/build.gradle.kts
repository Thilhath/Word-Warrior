plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.wordworrior"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wordworrior"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Android libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.13.3")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")  // For Java
}
