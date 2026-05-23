@file:OptIn(ExperimentalTime::class)

package dev.upcraft.gradle.multiloader

import org.gradle.kotlin.dsl.get
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

plugins {
    `java-library`
    `maven-publish`
}

val libs = versionCatalogs.named("libs")

val minecraftVersion: String = libs.findVersion("minecraft").orElseThrow().toString()
println("Minecraft: $minecraftVersion")

val javaVersion = libs.findVersion("java").orElseThrow().toString().toInt()
println("Java: $javaVersion")

val now = Instant.fromEpochSeconds(Clock.System.now().epochSeconds)

// FIXME workaround for appdirs transitively requiring newer JNA but we are locked due to MC
libs.findLibrary("jna").ifPresent {
    configurations.configureEach {
        resolutionStrategy.force(it)
    }
}

sourceSets {
    register("testmod") {
        java {
            compileClasspath += sourceSets["main"].compileClasspath
            runtimeClasspath += sourceSets["main"].runtimeClasspath
        }
    }
}

repositories {
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven(uri("https://maven.parchmentmc.org")) {
                name = "ParchmentMC"
            }
        }
        filter {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }

    // mod platforms
    exclusiveContent {
        forRepository {
            maven(uri("https://api.modrinth.com/maven")) {
                name = "Modrinth"
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    exclusiveContent {
        forRepository {
            maven(uri("https://www.cursemaven.com")) {
                name = "CurseMaven"
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }

    maven(uri("https://maven.fabricmc.net")) {
        name = "FabricMC"
    }

    maven(uri("https://maven.neoforged.net/releases")) {
        name = "NeoForge"
    }

    maven(uri("https://repo.spongepowered.org/repository/maven-public")) {
        name = "Sponge"
        content {
            includeGroupAndSubgroups("org.spongepowered")
        }
    }

    maven(uri("https://maven.uuid.gg/releases"))

    // third party maven repositories
    // TODO set up pass-through relay for these
    maven(uri("https://maven.ladysnake.org/releases")) {
        name = "Ladysnake"
    }

    maven(uri("https://maven.teamresourceful.com/repository/maven-releases")) {
        name = "TeamResourceful"
    }

    maven(uri("https://maven.terraformersmc.com/releases")) {
        name = "TerraformersMC"
    }
}

dependencies {

}

base {
    archivesName = "${rootProject.name}-${project.name}-${minecraftVersion}"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
        vendor = JvmVendorSpec.MICROSOFT
    }

    withSourcesJar()
    if(project.javadocEnabled) {
        withJavadocJar()
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion)
    options.compilerArgs.add("-Xlint:unchecked")
}

tasks.named<Jar>("jar").configure {
    from(rootProject.file("LICENSE.md")) {
        rename("LICENSE.md", "LICENSE_${rootProject.name}.md")
    }

    manifest.attributes(mapOf<String, Any>(
        "Specification-Title" to rootProject.name,
        "Specification-Vendor" to "Up",
        "Specification-Version" to archiveVersion,

        "Implementation-Title" to "${rootProject.name}-${project.name}",
        "Implementation-Vendor" to "Up",
        "Implementation-Version" to archiveVersion,
        "Implementation-Timestamp" to now.toString(),
        "Timestamp" to now.toEpochMilliseconds(),

        "Built-On-Java" to "${providers.systemProperty("java.vm.version").orNull} (${providers.systemProperty("java.vm.vendor").orNull})",
        "Built-On-Minecraft" to minecraftVersion
    ))
}

tasks.named<Jar>("sourcesJar").configure {
    from(rootProject.file("LICENSE.md")) {
        rename("LICENSE.md", "LICENSE_${rootProject.name}.md")
    }
}

tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).tags(listOf("reason", "implNote"))
}

tasks.named<Test>("test").configure {
    useJUnitPlatform()
}

tasks.withType<ProcessResources>().configureEach {
    filteringCharset = "UTF-8"
}

listOf("processResources", "processTestmodResources").forEach {
    tasks.named<ProcessResources>(it).configure {
        configureModProperties()
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}

// Declare capabilities on the outgoing configurations.
// Read more about capabilities here: https://docs.gradle.org/current/userguide/component_capabilities.html#sec:declaring-additional-capabilities-for-a-local-component
val cfgs = mutableListOf("apiElements", "runtimeElements", "sourcesElements")
if(project.javadocEnabled) {
    cfgs.add("javadocElements")
}

cfgs.forEach { variant ->
    configurations.named(variant).configure {
        outgoing {
            capability("$group:${rootProject.name}:$version")
            capability("$group:${rootProject.name}-${minecraftVersion}:$version")
            capability("$group:${project.name}-${minecraftVersion}:$version")
        }
    }

    publishing {
        publications.withType<MavenPublication>().configureEach {
            suppressPomMetadataWarningsFor(variant)
        }
    }
}
