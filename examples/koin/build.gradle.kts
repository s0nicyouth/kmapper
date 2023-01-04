val kMapperVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
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

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":processor_annotations"))
    implementation(project(":converters"))
    ksp(project(":processor"))

    val koinVersion = "3.3.2"
    val koinKspVersion = "1.1.0"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-annotations:$koinKspVersion")
    ksp("io.insert-koin:koin-ksp-compiler:$koinKspVersion")
}