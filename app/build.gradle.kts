plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "dev.busung.s25uroot"
    compileSdk = 37
    ndkVersion = "29.0.14206865"

    defaultConfig {
        minSdk = 33
        targetSdk = 36
        versionCode = 341
        versionName = "1.0.1-s928b-dzdp-offline-ksunext-3.4.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += "arm64-v8a"
        }

        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=none"
            }
        }
    }

    flavorDimensions += "manager"
    productFlavors {
        create("standard") {
            dimension = "manager"
            applicationId = "io.github.vishesh093.ksunextroot"
            versionNameSuffix = "-standard"
            buildConfigField("String", "KSU_MANAGER_PACKAGE", "\"com.rifsxd.ksunext\"")
            buildConfigField("String", "KSU_MANAGER_SHA256", "\"50339a93c0f812b8a72c1a387a1b441891e3df0f20b2d9daf80fd798d04b3de8\"")
            manifestPlaceholders["ksuManagerPackage"] = "com.rifsxd.ksunext"
        }
        create("spoofed") {
            dimension = "manager"
            applicationId = "io.github.vishesh093.ksunextroot.spoofed"
            versionNameSuffix = "-spoofed"
            buildConfigField("String", "KSU_MANAGER_PACKAGE", "\"yhaxhr.birgvn.bmwbne\"")
            buildConfigField("String", "KSU_MANAGER_SHA256", "\"84558aca2f82367f66534a7776aacb08f71d19fcb4e82d712c689d961f0e602b\"")
            manifestPlaceholders["ksuManagerPackage"] = "yhaxhr.birgvn.bmwbne"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        jniLibs.useLegacyPackaging = true
        jniLibs.keepDebugSymbols += "**/libcve43499*.so"
        jniLibs.keepDebugSymbols += "**/libcve43499root.so"
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    lint {
        checkReleaseBuilds = false
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
        )
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.05.01"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.5.0-alpha24")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")

    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core-ktx:1.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:runner:1.7.0")
}
