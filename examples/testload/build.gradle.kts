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

    // uncomment this if you want to work on processor and example project at once (no need to redeploy the project after every processor change)
//    implementation(project(":processor_annotations"))
//    implementation(project(":converters"))
//    ksp(project(":processor"))
}