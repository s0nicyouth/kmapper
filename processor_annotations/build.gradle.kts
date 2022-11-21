val kMapperVersion: String by project

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "com.syouth.kmapper.processor_annotations"
version = kMapperVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.syouth.kmapper"
            artifactId = "processor_annotations"
            version = version

            from(components["java"])
        }
    }
}
