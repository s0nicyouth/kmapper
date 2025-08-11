package com.syouth.kmapper

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KmapperGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {

            plugins.findPlugin("com.google.devtools.ksp") ?: error(
                "KSP plugin not found" +
                        " or specified later in the plugins block"
            )

            val kspExtension = extensions.getByName("ksp")

            val argMethod =
                kspExtension.javaClass.getMethod("arg", String::class.java, String::class.java)

            afterEvaluate {
                /**
                 * This is currently a workaround for a bug in KSP that causes the plugin
                 * to not work with multiplatform projects with only one target.
                 * https://github.com/google/ksp/issues/1525
                 */

                tasks.findByName("compileKotlinMetadata")
                    ?.dependsOn("kspCommonMainKotlinMetadata")


                if (kotlinExtension is KotlinSingleTargetExtension<*>) {

                    argMethod.invoke(
                        kspExtension,
                        "Kmapper_MultiplatformWithSingleTarget",
                        true.toString()
                    )
                } else {
                    val useKsp2 =
                        project.findProperty("ksp.useKSP2")?.toString()?.toBoolean()
                            ?: false

                    if (useKsp2) {
                        tasks.named { name -> name.startsWith("ksp") }.configureEach {
                            if (name != "kspCommonMainKotlinMetadata") {
                                dependsOn("kspCommonMainKotlinMetadata")
                            }
                        }
                    } else {
                        tasks.withType(KotlinCompilationTask::class.java).configureEach {

                            if (name != "kspCommonMainKotlinMetadata") {
                                dependsOn("kspCommonMainKotlinMetadata")
                            }
                        }
                    }
                }
            }

            val dependency = project(":processor")
            dependencies.add("kspCommonMainMetadata", dependency)


            when (val kotlinExtension = kotlinExtension) {
                is KotlinSingleTargetExtension<*> -> {
                    dependencies.add("ksp", dependency)
                }

                is KotlinMultiplatformExtension -> {
//                    kotlinExtension.targets.configureEach {
//                        if (platformType.name == "common") {
//                            dependencies.add("kspCommonMainMetadata", dependency)
//                            return@configureEach
//                        }
//                        val capitalizedTargetName =
//                            targetName.replaceFirstChar {
//                                if (it.isLowerCase()) {
//                                    it.titlecase(
//                                        US,
//                                    )
//                                } else {
//                                    it.toString()
//                                }
//                            }
//                        dependencies.add("ksp$capitalizedTargetName", dependency)
//
//                        if (this.compilations.any { it.name == "test" }) {
//                            dependencies.add("ksp${capitalizedTargetName}Test", dependency)
//                        }
//                    }


                    kotlinExtension.sourceSets
                        .getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME) {
                            kotlin.srcDir(
                                "${layout.buildDirectory.get()}/generated/ksp/metadata/" +
                                        "${KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME}/kotlin"
                            )
                        }

                }

                else -> Unit

            }
        }
    }
}