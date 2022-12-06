val kspVersion: String by project
val kMapperVersion: String by project

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.s0nicyouth"
version = kMapperVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("src/test/kotlin")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.s0nicyouth"
            artifactId = "converters"
            version = version

            from(components["java"])

            pom {
                name.set("kMapper converters")
                description.set("Converters for kMapper library")
                url.set("https://github.com/s0nicyouth/kmapper")
                scm { url.set("https://github.com/s0nicyouth/kmapper/tree/master/converters") }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("syouth")
                        name.set("Anton Ivanov")
                        email.set("mynameisantik@gmail.com")
                    }
                }
            }
        }
    }
    repositories {
        if (version.toString().endsWith("SNAPSHOT")) {
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
                name = "sonatypeReleaseRepository"
                credentials(PasswordCredentials::class)
            }
        } else {
            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
                name = "sonatypeSnapshotRepository"
                credentials(PasswordCredentials::class)
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}