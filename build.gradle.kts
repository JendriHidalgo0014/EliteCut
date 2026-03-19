// Elevar la versión de KGP y KSP por encima del mínimo de AGP 9.0 (2.2.10)
// Ref: https://developer.android.com/build/releases/agp-9-0-0-release-notes
buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)   // KGP 2.3.10
        classpath(libs.ksp.gradle.plugin)      // KSP 2.3.6
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // NO se aplica kotlin-android → AGP 9.0 lo trae integrado
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.room) apply false
}