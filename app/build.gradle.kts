

import java.util.Properties
plugins {
    id("kotlin-kapt")
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties()
val localPropertiesFile = File(rootDir , "secret.properties")

if(localPropertiesFile.exists() && localPropertiesFile.isFile){
    localPropertiesFile.inputStream().use {
        localProperties.load(it)

    }
}
android {
    namespace = "com.example.growwassignment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.growwassignment"
        minSdk = 24
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
            buildConfigField("String","ALPHA_VANTAGE_API_KEY","\"${localProperties.getProperty("ALPHA_VANTAGE_API_KEY")}\"")
            buildConfigField("String","ALPHA_VANTAGE_BASE_URL","\"${localProperties.getProperty("ALPHA_VANTAGE_BASE_URL")}\"")

        }
        debug {
            buildConfigField("String","ALPHA_VANTAGE_API_KEY","\"${localProperties.getProperty("ALPHA_VANTAGE_API_KEY")}\"")
            buildConfigField("String","ALPHA_VANTAGE_BASE_URL","\"${localProperties.getProperty("ALPHA_VANTAGE_BASE_URL")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)


    // hilt
    implementation(libs.hiltLibrary)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    kapt(libs.hiltKapt)

    // retrofit

    implementation (libs.retrofitLibrary)
    implementation (libs.gsonConverter)

    // swipe refresh
    implementation(libs.swipeRefreshLibrary)
    // Paging
    implementation(libs.pagingLibrary)

    //okhttp

    implementation(libs.okhttpLibrary)
    implementation(libs.okhttpInterceptorLibrary)
    
    implementation(libs.compose.navigation)

    // line graph MpAndroidChart
    implementation(libs.mpAndroidChart)

    // for flow layout
    implementation(libs.accompanistLibrary)

    implementation(libs.roomDB)
    kapt(libs.roomKapt)

    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}