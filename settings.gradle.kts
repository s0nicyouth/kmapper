enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("kmapper_plugin")
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

rootProject.name = "KMapper"

include(
    ":processor",
    ":processor_annotations",
    ":converters",
    ":examples:koin",
    ":examples:anvil",
    ":examples:sampleApplication:composeApp"
)

