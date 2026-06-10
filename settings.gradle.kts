pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "NeoForge"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
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
