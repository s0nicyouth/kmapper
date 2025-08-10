import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    `maven-publish`
    signing
}

group = "io.github.s0nicyouth"
version = libs.versions.kMapperVersion.get()

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}



publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.s0nicyouth"
            artifactId = "processor_annotations"
            version = version

            artifact(tasks["sourcesJar"])

            pom {
                name.set("kMapper annotations")
                description.set("Annotations for kMapper library")
                url.set("https://github.com/s0nicyouth/kmapper")
                scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/processor_annotations") }
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
    }
    repositories {
        if (version.toString().endsWith("SNAPSHOT")) {
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
                name = "sonatypeSnapshotRepository"
                credentials(PasswordCredentials::class)
            }
        } else {
            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
                name = "sonatypeReleaseRepository"
                credentials(PasswordCredentials::class)
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
