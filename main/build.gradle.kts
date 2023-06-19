plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 16
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

        getByName("debug") {
            isMinifyEnabled = false

            matchingFallbacks.add("release")
        }
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        java.srcDirs("src/androidMain/kotlin")
        res.srcDirs("src/androidMain/res")
    }

    packagingOptions {
        resources {
            excludes += setOf("META-INF/kotlinx-coroutines-core.kotlin_module")
        }
    }
}

kotlin {
    //todo: add IOS
    //todo: add Web

    android("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("dev.icerock.moko:mvvm-core:0.15.0")
        implementation("dev.icerock.moko:mvvm-livedata:0.15.0")
    }

    sourceSets["androidMain"].dependencies {
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("com.google.firebase:firebase-firestore:24.0.1")
    }
}