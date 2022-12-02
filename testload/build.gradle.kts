val kMapperVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.google.devtools.ksp")
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

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/main/kotlin")
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}