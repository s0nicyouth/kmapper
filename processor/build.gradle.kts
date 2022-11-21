val kspVersion: String by project
val kMapperVersion: String by project

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "com.syouth.kmapper.processor"
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
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

    implementation("com.syouth.kmapper:processor_annotations:$version")

    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
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
            artifactId = "processor"
            version = version

            from(components["java"])
        }
    }
}