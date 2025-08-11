plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
}

version = "1.0"

application {
    mainClass.set("com.syouth.kmapper.koin.Main")
}

ksp {
    arg("injector", "koin")
    arg("koinInjectionType", "single")
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

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

}