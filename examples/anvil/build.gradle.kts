plugins {
    application
    kotlin("jvm")
    kotlin("kapt")
    alias(libs.plugins.ksp)
    id("com.squareup.anvil") version ("2.6.1")
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(projects.processorAnnotations)
    implementation(projects.converters)
    ksp(projects.processor)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}