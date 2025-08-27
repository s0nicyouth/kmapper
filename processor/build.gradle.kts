import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm")
    `java-library`
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

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.ksp.api)

    implementation(libs.processor.annotations)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlin.compile.testing)

}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("src/test/kotlin")
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("kMapper processor")
        description.set("kMapper library")
        url.set("https://github.com/s0nicyouth/kmapper")
        scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/processor") }
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

