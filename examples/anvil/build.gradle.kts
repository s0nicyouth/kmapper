val kMapperVersion: String by project

plugins {
    application
    kotlin("jvm")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.squareup.anvil") version("2.4.5")
}

repositories {
    mavenCentral()
    mavenLocal()
}

version = "1.0"

application {
    mainClass.set("com.syouth.kmapper.anvil.Main")
}

ksp {
    arg("injector", "anvil")
    arg("anvilBindingScope", "com.syouth.kmapper.anvil.AppScope")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

    implementation("com.google.dagger:dagger:2.44.2")
    kapt("com.google.dagger:dagger-compiler:2.44.2")
}