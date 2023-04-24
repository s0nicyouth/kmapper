pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.8.20-1.0.11"
        kotlin("jvm") version "1.8.20"
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "KMapper"

include(
    ":processor",
    ":processor_annotations",
    ":converters",
    ":examples:testload",
    ":examples:koin",
    ":examples:anvil",
)

