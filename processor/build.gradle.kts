val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "com.syouth.kmapper.processor"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

    implementation(project(":processor_annotations"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}