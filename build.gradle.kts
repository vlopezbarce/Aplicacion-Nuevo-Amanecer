// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.0")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}