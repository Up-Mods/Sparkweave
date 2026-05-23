package dev.upcraft.gradle.multiloader

import gradle.kotlin.dsl.accessors._3c984467cfe6063166439ec0710b6c00.sourceSets
import gradle.kotlin.dsl.accessors._3c984467cfe6063166439ec0710b6c00.versionCatalogs
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyScopeConfiguration
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.HasAttributes
import org.gradle.api.attributes.HasConfigurableAttributes
import org.gradle.kotlin.dsl.getByType
import org.gradle.language.jvm.tasks.ProcessResources
import kotlin.collections.mapOf
import kotlin.jvm.optionals.getOrNull

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

/**
 * [MC Gradle Conventions](https://github.com/mcgradleconventions)
 */
public fun Project.applyMcGradleConventions(loader: String, configurations: List<String>? = null) {
    afterEvaluate {
        val cfgs = buildList {
            if(configurations != null) {
                addAll(configurations)
            }
            else {
                add("apiElements")
                add("runtimeElements")
                add("sourcesElements")
                add("includeInternal")
                add("modCompileClasspath")
                add("javadocElements")
            }

            sourceSets.forEach {
                add(it.compileClasspathConfigurationName)
                add(it.runtimeClasspathConfigurationName)
            }
        }
        cfgs.forEach {
            this.configurations.findByName(it)?.attributes {
                attribute(loaderAttribute, loader)
            }
        }
    }
}

public fun ProcessResources.configureModProperties() {
    val libs = project.versionCatalogs.named("libs")
    val expandProps = mapOf(
        "version" to project.version,
        "maven_group_id" to project.group,
        "mod_id" to project.property("mod_id"),
        "mod_display_name" to project.property("mod_display_name"),
        "mod_description" to project.property("mod_description"),
        "sources_url" to project.property("sources_url"),
        "issues_url" to project.property("issues_url"),
        "license_url" to project.property("license_url"),
        "discord_url" to project.property("discord_url"),
        "homepage_url" to project.property("homepage_url"),
        "curseforge_id" to project.property("curseforge_id"),
        "modrinth_id" to project.property("modrinth_id"),

        "java_version" to libs.findVersion("java").orElseThrow(),
        "minecraft_version" to libs.findVersion("minecraft").orElseThrow(),
        "neoforge_version" to libs.findVersion("neoforge").getOrNull(),
        "fabric_loader_version" to libs.findVersion("fabric_loader").getOrNull(),
    )

    filesMatching("META-INF/*mods.toml") {
        expand(expandProps)
    }

    filesMatching(listOf("pack.mcmeta", "*.mod.json", "*.mixins.json")) {
        expand(expandProps.mapValues { it.value.toString().replace("\n", "\\n") })
    }

    inputs.properties(expandProps)
}

internal val Project.javadocEnabled get() = property("dev.upcraft.gradle.multiloader.enableJavadoc").toString().toBoolean()
