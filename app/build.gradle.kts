import org.gradle.api.tasks.testing.logging.TestLogEvent;

plugins {
    alias(libs.plugins.android.application)
    id("de.mannodermaus.android-junit5") version "1.8.2.1"
}

android {
    namespace = "com.ramonpapes.testmediaequalizerservice"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ramonpapes.testmediaequalizerservice"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild{
            cmake{
                cppFlags.add("-std=c++11")
            }
        }
    }

    externalNativeBuild{
        cmake{
            version = "3.10.2"
        }
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

    testOptions{
        unitTests.all {
            it.testLogging{
                events = setOf(
                    TestLogEvent.FAILED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.PASSED
                )
            }
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.media:media:1.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")

}