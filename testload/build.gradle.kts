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
    ksp(project(":processor"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}