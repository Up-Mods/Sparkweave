@file:OptIn(ExperimentalTime::class)
@file:Suppress("UnstableApiUsage")

package dev.upcraft.gradle.multiloader

import kotlin.time.ExperimentalTime

plugins {
    `java-library`
    `maven-publish`
}

version = rootProject.version

val libs = versionCatalogs.named("libs")

val minecraftVersion: String = libs.findVersion("minecraft").orElseThrow().toString()
println("Minecraft: $minecraftVersion")

val javaVersion = libs.findVersion("java").orElseThrow().toString().toInt()
println("Java: $javaVersion")

val now = providers.of(BuildTimeValueSource::class) {}

// FIXME workaround for appdirs transitively requiring newer JNA but we are locked due to MC
libs.findLibrary("jna").ifPresent {
    configurations.configureEach {
        resolutionStrategy.force(it)
    }
}

val testmod = sourceSets.register("testmod") {
    java {
        compileClasspath += sourceSets["main"].compileClasspath
        runtimeClasspath += sourceSets["main"].runtimeClasspath
    }
}

repositories {
    mavenCentral()

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

    maven(uri("https://maven.uuid.gg/releases"))

    // third party maven repositories
    // TODO set up pass-through relay for these
    maven(uri("https://maven.ladysnake.org/releases")) {
        name = "Ladysnake"
    }

    maven(uri("https://maven.teamresourceful.com/repository/maven-releases")) {
        name = "TeamResourceful"
    }

    // FIXME currently unavailable, using backup
    // maven(uri("https://maven.terraformersmc.com/releases")) {
    maven(uri("https://maven.gnomecraft.net/releases")) {
        name = "TerraformersMC"
    }

    maven(uri("https://maven.blamejared.com")) {
        name = "BlameJared"
    }

    maven((uri("https://modmaven.dev"))) {
        name = "ModMaven"
    }
}

dependencies {
    compileOnly(libs.findLibrary("jetbrains_annotations").orElseThrow())
    compileOnly(libs.findLibrary("jspecify").orElseThrow())

    compileOnly(libs.findLibrary("autoservice_annotations").orElseThrow())
    annotationProcessor(libs.findLibrary("autoservice").orElseThrow())

    "testmodCompileOnly"(libs.findLibrary("autoservice_annotations").orElseThrow())
    "testmodAnnotationProcessor"(libs.findLibrary("autoservice").orElseThrow())
    "testmodImplementation"(sourceSets["main"].output)

    testImplementation(libs.findLibrary("junit_api").orElseThrow())
    testRuntimeOnly(libs.findLibrary("junit_launcher").orElseThrow())
    testRuntimeOnly(libs.findLibrary("junit_engine").orElseThrow())
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
        vendor = JvmVendorSpec.MICROSOFT
    }

    withSourcesJar()
    if (project.javadocEnabled) {
        withJavadocJar()
    }

    registerFeature("testmod") {
        usingSourceSet(testmod.get())
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion)
    options.compilerArgs.add("-Xlint:unchecked")
}

val tmpManifest = tasks.register<BuildTimeManifestTask>("createManifestTimestamp") {
    description = "Create manifest file with build timestamp"
    buildTime = now
}

tasks.named<Jar>("jar").configure {

    from(rootProject.file("LICENSE.md")) {
        rename("LICENSE.md", "LICENSE_${rootProject.name}.md")
    }

    manifest.from(tmpManifest.flatMap { it.manifestPath })
    dependsOn(tmpManifest)

    manifest.attributes(
        mapOf<String, Any>(
            "Specification-Title" to rootProject.name,
            "Specification-Vendor" to "Up",
            "Specification-Version" to archiveVersion,

            "Implementation-Title" to project.name,
            "Implementation-Vendor" to "Up",
            "Implementation-Version" to archiveVersion,

            "Built-On-Java" to "${providers.systemProperty("java.vm.version").orNull} (${providers.systemProperty("java.vm.vendor").orNull})",
            "Built-On-Minecraft" to minecraftVersion
        )
    )
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

    providers.environmentVariable("MAVEN_UPLOAD_URL").orNull?.let { url ->
        publishing {
            repositories {
                maven(uri(url)) {
                    credentials {
                        username = providers.environmentVariable("MAVEN_UPLOAD_USERNAME").orNull
                        password = providers.environmentVariable("MAVEN_UPLOAD_PASSWORD").orNull
                    }
                }
            }
        }
    }
}

// Declare capabilities on the outgoing configurations.
// Read more about capabilities here: https://docs.gradle.org/current/userguide/component_capabilities.html#sec:declaring-additional-capabilities-for-a-local-component
buildList {
    add("apiElements")
    add("runtimeElements")
    add("sourcesElements")

    if (project.javadocEnabled) {
        add("javadocElements")
    }
}.forEach { variant ->
    configurations.named(variant).configure {
        outgoing {
            capability("$group:${project.name}:$version")
            capability("$group:${rootProject.name}:$version")
        }
    }

    publishing {
        publications.withType<MavenPublication>().configureEach {
            suppressPomMetadataWarningsFor(variant)
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        listOf("testmodApiElements", "testmodRuntimeElements").forEach {
            suppressPomMetadataWarningsFor(it)
        }
    }
}
