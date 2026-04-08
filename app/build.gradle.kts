plugins {
    alias(libs.plugins.android.application)
    // NO se aplica kotlin-android → AGP 9.0 lo trae integrado
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
}

android {
    namespace = "ucne.edu.elitecut"
    compileSdk = 36

    defaultConfig {
        applicationId = "ucne.edu.elitecut"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // NOTA: kotlinOptions{} ya no se usa con AGP 9.0 built-in Kotlin
    // Se reemplaza por el bloque kotlin{} de abajo

    buildFeatures {
        compose = true
    }


}

room {
    schemaDirectory("$projectDir/schemas")
}

// Reemplazo de kotlinOptions para AGP 9.0 built-in Kotlin
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {

    // ── AndroidX Core ──
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)

    // ── Compose (BOM maneja todas las versiones) ──
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation)

    // ── Navigation ──
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // ── Room (todo via KSP) ──
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.tv.material)
    ksp(libs.room.compiler)

    // ── Hilt (todo via KSP, sin kapt) ──
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)

    // ── Retrofit / Network ──
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)

    // ── Images ──
    implementation(libs.coil.compose)

    // ── Utils ──
    implementation(libs.gson)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // ── WorkManager ──
    implementation(libs.work.runtime.ktx)

    // ══════════════════════════════════════
    // ── Testing ──
    // ══════════════════════════════════════

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.arch.core.testing)

    // Android Instrumented Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose Testing (versiones manejadas por BOM)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt Testing (todo via KSP, sin kapt)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
}