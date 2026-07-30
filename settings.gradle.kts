pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.neoforged.net/releases") {
            name = "NeoForge"
        }
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        maven("https://maven.uuid.gg/releases") {
            name = "Up-Mods"
        }
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.upcraft.gradle.multiloader.settings") version "0.1.3"
}

rootProject.name = "Sparkweave"

includeBuild("build-logic")

listOf("Common", "Fabric", "NeoForge").forEach {
    include(it)
    project(":$it").name = "${rootProject.name}-$it"
}

buildCache {
    remote<HttpBuildCache> {
        url = uri("https://ci-cache.uuid.gg/cache")
        val pass = providers.environmentVariable("GRADLE_BUILD_CACHE_TOKEN")
        isPush = providers.environmentVariable("CI").orNull.toBoolean() && pass.isPresent
        credentials {
            username = providers.environmentVariable("GRADLE_BUILD_CACHE_USER").orNull
            password = pass.orNull
        }
    }
}
