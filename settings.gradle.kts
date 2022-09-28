pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.7.10-1.0.6"
        kotlin("jvm") version "1.7.10"
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KMapper"

include(
    ":processor",
    ":processor_annotations",
    ":converters",
    ":testload"
)

