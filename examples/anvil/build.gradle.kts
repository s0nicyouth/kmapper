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

application {
    mainClass.set("com.syouth.kmapper.anvil.Main")
}

ksp {
    arg("injector", "anvil")
    arg("anvilBindingScope", "com.syouth.kmapper.anvil.AppScope")
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(projects.processorAnnotations)
    implementation(projects.converters)
    ksp(projects.processor)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}