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
    implementation(project(":processor"))
    implementation(project(":processor_annotations"))
    implementation(project(":converters"))
    ksp(project(":processor"))
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