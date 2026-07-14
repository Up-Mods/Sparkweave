@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions
import net.fabricmc.loom.task.LoomTasks
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings


plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("org.jetbrains.gradle.plugin.idea-ext")
    id("net.fabricmc.fabric-loom")
}
applyMcGradleConventions("fabric")

val modID: String = providers.gradleProperty("mod_id").get()

repositories {
    exclusiveContent {
        forRepository {
            maven(uri("https://maven.covers1624.net")) {
                name = "Covers1624"
            }
        }
        filter {
            includeGroup("net.covers1624")
        }
    }
}

dependencies {
    localRuntime(libs.devlogin)
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

//	implementation(libs.resourcefulconfig.fabric) {
//      isTransitive = false
//	}

    implementation(libs.appdirs)
    include(libs.appdirs)

    compileOnly(libs.jei.fabric.api)
    localRuntime(libs.jei.fabric)

    compileOnly(libs.modmenu.fabric) {
        isTransitive = false
    }
    localRuntime(libs.modmenu.fabric) {
        isTransitive = false
    }
}

loom {
    mods {
        create(modID) {
            // Tell Loom about each source set used by your mod here. This ensures that your mod's classes are properly transformed by Loader.
            sourceSet("main")
        }

        create("${modID}_testmod") {
            sourceSet("testmod")
        }
    }

    accessWidenerPath.set(file("src/main/resources/${modID}.classtweaker"))

    runs {
        named("client") {
            client()
            programArguments.addAll(listOf("--launch_target", "net.fabricmc.loader.impl.launch.knot.KnotClient"))
            mainClass = "net.covers1624.devlogin.DevLogin"
            displayName = "Fabric Client"
            runDirectory = file("run/client")
        }

        create("testmodClient") {
            client()
            programArguments.addAll(listOf("--launch_target", "net.fabricmc.loader.impl.launch.knot.KnotClient"))
            mainClass = "net.covers1624.devlogin.DevLogin"
            displayName = "Fabric TestmodClient"
            runDirectory = file("run/testmod")
            sourceSet = "testmod"
        }

        named("server") {
            server()
            displayName = "Fabric Server"
            runDirectory = file("run/server")
        }

        fabricApi.configureDataGeneration {
            outputDirectory = file("src/testmod/generated")
            addToResources = false

            client = true
            modId = modID
            strictValidation = true // '--all' sets '--validate' to true as well
        }

        named("datagen") {
            displayName = "Fabric TestmodData"

            sourceSet = "testmod"

            systemProperties.put("sparkweave.datagen.mods", "${modID}_testmod")
        }

        configureEach {
            appendProjectPathToDisplayName = false
            systemProperties.put("fabric-tag-conventions-v2.missingTagTranslationWarning", "VERBOSE")
            systemProperties.put("sparkweave.debug", "true")
            systemProperties.put("mixin.debug", "true")

            // register as Gradle runs instead of IDEA runs
            // https://github.com/FabricMC/fabric-loom/issues/1349
            generateRunConfig = false
            rootProject.idea.project.settings.runConfigurations.create<org.jetbrains.gradle.ext.Gradle>(displayName.get()) {
                taskNames = listOf(LoomTasks.getRunConfigTaskName(this@configureEach))
                setProject(project)
            }
        }
    }
}

sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
