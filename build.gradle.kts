@file:OptIn(ExperimentalTime::class)

import kotlin.time.ExperimentalTime

plugins {
    idea
    `maven-publish`
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.moddevgradle) apply false
    alias(libs.plugins.fabric.loom.remap) apply false
}

val tag = providers.environmentVariable("TAG")
val isPreviewBuild = tag.orNull?.matches(Regex(".+-.+")) ?: false
val buildNumber = tag.orElse(providers.environmentVariable("BUILD_NUMBER").map { "build.${it}" })

version = tag.orElse(provider { buildString {
    append("development")
    if(isPreviewBuild && !tag.isPresent) {
        append(buildNumber.map { "+${it}" }.getOrElse(""))
    }
} }).get()

println("Building ${project.name} $version")

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

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
	}
}
