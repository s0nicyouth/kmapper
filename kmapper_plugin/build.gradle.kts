import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("java-gradle-plugin")
    `kotlin-dsl`
    `maven-publish`
    signing
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://maven.google.com")
        maven("https://plugins.gradle.org/m2/")
        google()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    add("compileOnly", kotlin("gradle-plugin"))
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.2.0")
}

gradlePlugin {
    website.set("https://github.com/s0nicyouth/kmapper")
    vcsUrl.set("https://github.com/s0nicyouth/kmapper")
    plugins {
        register("kmapperPlugin") {
            id = "com.syouth.kmapper" // users will do `apply plugin: "com.syouth.kmapper"`
            implementationClass = "com.syouth.kmapper.KmapperGradlePlugin" // entry-point class
            displayName = "Kmapper Gradle Plugin"
            description = "Gradle Plugin for Kmapper"
            version = "0.0.1"
            tags.set(listOf("mapper", "kotlin", "kotlin-mapper", "java-struct", "rest"))
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "io.github.s0nicyouth"
//            artifactId = "kmapper.gradle.plugin"
//            version = version
//
//            from(components["java"])
//
//            pom {
//                name.set("kMapper annotations")
//                description.set("plugin for kMapper library")
//                url.set("https://github.com/s0nicyouth/kmapper")
//                scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/processor_annotations") }
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("syouth")
//                        name.set("Anton Ivanov")
//                        email.set("mynameisantik@gmail.com")
//                    }
//                }
//            }
//        }
//    }
//    repositories {
//        if (version.toString().endsWith("SNAPSHOT")) {
//            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
//                name = "sonatypeSnapshotRepository"
//                credentials(PasswordCredentials::class)
//            }
//        } else {
//            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
//                name = "sonatypeReleaseRepository"
//                credentials(PasswordCredentials::class)
//            }
//        }
//    }
//}
//
//signing {
//    sign(publishing.publications["maven"])
//}