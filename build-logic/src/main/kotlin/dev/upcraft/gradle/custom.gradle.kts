package dev.upcraft.gradle

import gradle.kotlin.dsl.accessors._3c984467cfe6063166439ec0710b6c00.versionCatalogs
import kotlin.time.ExperimentalTime

plugins {
    `java-library`
    `maven-publish`
}

val libs = versionCatalogs.named("libs")

// FIXME workaround for appdirs transitively requiring newer JNA but we are locked due to MC
libs.findLibrary("jna").ifPresent {
    configurations.configureEach {
        resolutionStrategy.force(it)
    }
}

repositories {
    // third party maven repositories
    // TODO set up pass-through relay for these
    maven("https://maven.ladysnake.org/releases") {
        name = "Ladysnake"
    }

    maven("https://maven.teamresourceful.com/repository/maven-releases") {
        name = "TeamResourceful"
    }

    // FIXME currently unavailable, using backup
    // maven("https://maven.terraformersmc.com/releases") {
    maven("https://maven.gnomecraft.net/releases") {
        name = "TerraformersMC"
    }

    maven("https://maven.blamejared.com") {
        name = "BlameJared"
    }

    maven("https://modmaven.dev") {
        name = "ModMaven"
    }
}
