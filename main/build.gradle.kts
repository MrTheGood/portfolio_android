plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(16)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

        getByName("debug") {
            isDebuggable = false
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
        implementation("dev.icerock.moko:mvvm:0.4.0")


        implementation("dev.icerock.moko:mvvm-core:0.16.0")
        implementation("dev.icerock.moko:mvvm-livedata:0.16.0")
    }

    sourceSets["androidMain"].dependencies {
        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("com.google.firebase:firebase-firestore:21.5.0")
    }
}