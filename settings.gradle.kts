pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.7.21-1.0.8"
        kotlin("jvm") version "1.7.21"
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
    ":testload"
)

