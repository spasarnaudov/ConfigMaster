import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("maven-publish")
}

val versionCode = 3
val versionName = "0.0.$versionCode"

android {
    namespace = "com.configmastersdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    lint {
        // Don’t run lint on test sources
        ignoreTestSources = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Rood
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp (libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //DataStore
    implementation(libs.androidx.datastore.preferences)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.spascoding"
            artifactId = "config-master-sdk"
            version = versionName

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    val localProperties = Properties().apply {
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            load(localPropsFile.inputStream())
        }
    }

    repositories {
        maven {
            name = "ConfigMasterSDK"
            url = uri("https://maven.pkg.github.com/spasarnaudov/ConfigMaster")
            credentials {
                username = localProperties.getProperty("gpr.user")
                password = localProperties.getProperty("gpr.key")
            }
        }
        mavenLocal()
    }
}

// --- Auto-publish tasks ---

// Automatically publish to local Maven repository after build
tasks.register("sdkBuildAndPublishLocal") {
    group = "publishing"
    description = "Build library and publish to local Maven repository"

    dependsOn("build")
    finalizedBy("publishToMavenLocal")
}

tasks.register("sdkBuildAndPublishGithub") {
    group = "publishing"
    description = "Build library and publish to GitHub Maven repository"

    dependsOn("build")
    finalizedBy("publish")
}