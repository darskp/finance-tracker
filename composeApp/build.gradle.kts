import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.serialization)
    alias(libs.plugins.googleServices)
    id("org.jetbrains.kotlinx.kover") version "0.9.1"


    id("io.github.takahirom.roborazzi") version "1.40.1"
}

kotlin {
    androidTarget()
    
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            // implementation(libs.androidx.room.sqlite.wrapper)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.androidx.splashscreen)
            implementation(libs.clerk.android.ui)
        }




        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            api(libs.datastore.preferences)
            api(libs.datastore)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.kotlinx.datetime)
            implementation(libs.gitlive.firebase.analytics)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.compose.ui.test)
            implementation(libs.ktor.client.mock)
        }
        
        val iosX64 = iosArm64()
        val iosSimulatorArm64 = iosSimulatorArm64()
        
        val iosMain by creating {
            dependsOn(commonMain.get())
            iosX64.compilations.getByName("main").defaultSourceSet.dependsOn(this)
            iosSimulatorArm64.compilations.getByName("main").defaultSourceSet.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.robolectric)
                implementation(libs.roborazzi)
                implementation(libs.roborazzi.compose)
                implementation(libs.roborazzi.junit)
            }
        }
    }
    sourceSets.all {
        languageSettings.optIn("kotlin.time.ExperimentalTime")
    }
}

android {
    namespace = "com.finvoraai.personalfinancemanager"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val keystoreProperties = Properties()
    val keystorePropertiesFile = rootProject.file("local.properties")
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    }

    defaultConfig {
        applicationId = "com.finvoraai.personalfinancemanager"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.1.0"
        buildConfigField("String", "CLERK_PUBLISHABLE_KEY", "\"${keystoreProperties["CLERK_PUBLISHABLE_KEY"]}\"")
        buildConfigField("String", "BACKEND_API_URL", "\"${keystoreProperties["BACKEND_API_URL"] ?: "https://expense-tracker-backend-eight-sandy.vercel.app/api"}\"")
    }
    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("finvoraai-release-key.jks")
            storePassword = keystoreProperties["RELEASE_STORE_PASSWORD"] as? String ?: ""
            keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"] as? String ?: "finvoraai"
            keyPassword = keystoreProperties["RELEASE_KEY_PASSWORD"] as? String ?: ""
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}
dependencies {
    implementation(platform(libs.firebase.bom))
    debugImplementation(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)

    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

