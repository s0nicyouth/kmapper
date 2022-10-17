val kMapperVersion: String by project

plugins {
    kotlin("jvm")
    `java-library`
}

group = "com.syouth.kmapper.processor_annotations"
version = kMapperVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
