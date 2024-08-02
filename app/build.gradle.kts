plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.b07project2024.group1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.b07project2024.group1"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.register("runAllTests") {
        dependsOn ("test")
        dependsOn ("connectedDebugAndroidTest")

        doLast {
            println("All tests executed successfully.")
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation (libs.androidx.fragment)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.glide)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.robolectric.robolectric)
    // ViewModel
    implementation(libs.lifecycle.viewmodel)
    // LiveData
    implementation(libs.lifecycle.livedata)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.lifecycle.runtime)
    // Saved state module for ViewModel
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.common.java8)
    // ReactiveStreams support for LiveData
    implementation(libs.androidx.lifecycle.reactivestreams)
    // Test helpers for LiveData
    testImplementation(libs.androidx.core.testing)
    // Test helpers for Lifecycle runtime
    testImplementation(libs.androidx.lifecycle.runtime.testing)
    implementation (libs.recyclerview.selection)
    //HILT
    implementation(libs.hilt.android)
    annotationProcessor (libs.com.google.dagger.hilt.compiler2)

    // For instrumentation tests
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestAnnotationProcessor(libs.com.google.dagger.hilt.compiler2)

    // For local unit tests
    testImplementation(libs.dagger.hilt.android.testing)
    testAnnotationProcessor(libs.com.google.dagger.hilt.compiler2)
}

