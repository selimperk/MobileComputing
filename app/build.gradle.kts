plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services) // Beibehalten, falls für Firebase notwendig
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
    implementation(libs.transportation.consumer) // Beibehalten, falls es verwendet wird
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Guava (bereits vorhanden)
    implementation("com.google.guava:guava:31.1-android")

    // Firebase (bereits vorhanden)
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")
    // HINWEIS: Wenn du Firebase Firestore für deine Daten synchronisierst,
    // könnte OkHttp für deine "DataSyncRepository"-Aufgabe überflüssig sein,
    // da Firebase seine eigene Netzwerk- und Synchronisationsschicht hat.
    // Falls du aber noch einen EIGENEN Server über HTTP/HTTPS ansteuerst,
    // dann ist OkHttp weiterhin sinnvoll!

    // Room (bereits vorhanden)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Navigation (bereits vorhanden)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // DataStore (bereits vorhanden)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    implementation("androidx.compose.material:material-icons-extended")

    // Dagger Hilt for Dependency Injection (bereits vorhanden)
    implementation(libs.androidx.hilt.work)
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.navigation.compose)

    // Kamera (bereits vorhanden)
    implementation("androidx.camera:camera-core:1.3.3")
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")

    // NEU: OkHttp - Eine robuste HTTP-Client-Bibliothek für sichere Netzwerkkommunikation
    // Wenn du EIGENE Server über HTTP/HTTPS ansteuerst und nicht nur Firebase nutzt,
    // ist diese Bibliothek sehr empfehlenswert.
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // Neueste stabile Version
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Für Netzwerk-Logging im Debug-Modus
}