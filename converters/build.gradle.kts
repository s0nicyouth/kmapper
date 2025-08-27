import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.gradleMavenPublish)
    signing
}

group = "io.github.s0nicyouth"
version = libs.versions.kMapperVersion.get()

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvm()

    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        nodejs()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosX64()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()
    mingwX64()
    linuxX64()
    linuxArm64()

}

dependencies {
    commonMainApi(libs.bignum)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("kMapper converters")
        description.set("Converters for kMapper library")
        url.set("https://github.com/s0nicyouth/kmapper")
        scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/converters") }
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("syouth")
                name.set("Anton Ivanov")
                email.set("mynameisantik@gmail.com")
            }
        }
    }
}