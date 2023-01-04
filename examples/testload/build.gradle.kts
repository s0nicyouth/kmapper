val kMapperVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
    mavenLocal()
}

version = "1.0"

application {
    mainClass.set("com.syouth.kmapper.testload.Main")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.s0nicyouth:processor_annotations:$kMapperVersion")
    implementation("io.github.s0nicyouth:converters:$kMapperVersion")
    ksp("io.github.s0nicyouth:processor:$kMapperVersion")
}