plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
 }

 android {
     namespace = "com.example.handson1st"
     compileSdk = 35

     defaultConfig {
         applicationId = "com.example.handson1st"
         minSdk = 24
         targetSdk = 35
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
         sourceCompatibility = JavaVersion.VERSION_17
         targetCompatibility = JavaVersion.VERSION_17
     }
     kotlinOptions {
         jvmTarget = "17"
     }
     buildFeatures {
         compose = true
     }
     composeOptions {
         kotlinCompilerExtensionVersion = "1.5.7"
     }

 }

 dependencies {
     implementation(libs.androidx.runtime.livedata)
     implementation(libs.androidx.datastore.core.android)
     implementation(libs.androidx.lifecycle.runtime.compose)
     implementation(libs.androidx.core.ktx)
     implementation(libs.androidx.lifecycle.runtime.ktx)
     implementation(libs.androidx.activity.compose)
     implementation(platform(libs.androidx.compose.bom))
     implementation(libs.androidx.ui)
     implementation(libs.androidx.ui.graphics)
     implementation(libs.androidx.ui.tooling.preview)
     implementation(libs.androidx.material3)
     implementation(libs.androidx.work.runtime.ktx)
     implementation(libs.transportation.consumer)
     implementation(libs.firebase.messaging)
     testImplementation(libs.junit)
     androidTestImplementation(libs.androidx.junit)
     androidTestImplementation(libs.androidx.espresso.core)
     androidTestImplementation(platform(libs.androidx.compose.bom))
     androidTestImplementation(libs.androidx.ui.test.junit4)
     debugImplementation(libs.androidx.ui.tooling)
     debugImplementation(libs.androidx.ui.test.manifest)

     // Room
     implementation(libs.androidx.room.ktx)
     implementation(libs.androidx.room.runtime)
     ksp(libs.androidx.room.compiler)

     // Navigation
     implementation(libs.androidx.navigation.compose)
     implementation(libs.kotlinx.serialization.json)


     // DataStore
     implementation(libs.androidx.datastore.preferences)
     implementation(libs.androidx.datastore)

     implementation("androidx.compose.material:material-icons-extended")

     implementation(libs.androidx.hilt.work)
     implementation(libs.dagger.hilt)
     kapt(libs.dagger.hilt.compiler)
     implementation(libs.dagger.hilt.navigation.compose)

     //Firebase
     implementation("com.google.firebase:firebase-messaging:25.0.0")
     implementation("com.squareup.okhttp3:okhttp:4.12.0")
 }
