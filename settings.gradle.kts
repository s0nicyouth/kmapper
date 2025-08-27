import java.net.URI

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
        maven {
            name = "Central Portal Snapshots"
            url = URI("https://central.sonatype.com/repository/maven-snapshots/")

            content {
                includeGroup("io.github.s0nicyouth")
            }
        }
        mavenCentral()
        mavenLocal()
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

