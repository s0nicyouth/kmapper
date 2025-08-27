import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("java-gradle-plugin")
    `kotlin-dsl`
    `maven-publish`
    signing
}



group = "io.github.s0nicyouth"
version = libs.versions.kMapperGradlePluginVersion.get()

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

dependencies {
    add("compileOnly", kotlin("gradle-plugin"))
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
}

gradlePlugin {
    website.set("https://github.com/s0nicyouth/kmapper")
    vcsUrl.set("https://github.com/s0nicyouth/kmapper")
    plugins {
        register("kmapperPlugin") {
            id = libs.plugins.kmapper.get().pluginId
            group = group
            implementationClass = "com.syouth.kmapper.KmapperGradlePlugin"
            displayName = "Kmapper Gradle Plugin"
            description = "Gradle Plugin for Kmapper"
            version = version
            tags.set(listOf("mapper", "kotlin", "kotlin-mapper", "java-struct", "rest"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = groupId
            artifactId = libs.plugins.kmapper.get().pluginId
            version = version

            from(components["java"])

            pom {
                name.set("kMapper Gradle Plugin")
                description.set("plugin for kMapper library")
                url.set("https://github.com/s0nicyouth/kmapper")
                scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/kmapper_plugin") }
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

