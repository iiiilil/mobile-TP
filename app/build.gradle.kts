plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    kotlin("kapt") // 이 줄을 추가하세요.
}

dependencies {
    implementation ("androidx.recyclerview:recyclerview:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-firestore:24.7.0")
    implementation ("com.google.firebase:firebase-firestore:24.7.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.material3.android)
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


android {
    namespace = "com.Refee.RefeeDB"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.Refee.RefeeDB"
        minSdk = 29
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}