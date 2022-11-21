val kspVersion: String by project
val kMapperVersion: String by project

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "com.syouth.kmapper.converters"
version = kMapperVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("src/test/kotlin")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.syouth.kmapper"
            artifactId = "converters"
            version = version

            from(components["java"])
        }
    }
}