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

val modId: String = providers.gradleProperty("mod_id").get()

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

    compileOnly(libs.autoservice.annotations)
    annotationProcessor(libs.autoservice)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    testmodImplementation(sourceSets["main"].output)
    testmodCompileOnly(libs.autoservice.annotations)
    testmodAnnotationProcessor(libs.autoservice)

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

    testImplementation(libs.junit.api)
    testImplementation(libs.neoforge.testframework)
    testRuntimeOnly(libs.junit.launcher)
    testRuntimeOnly(libs.junit.engine)
}

loom {
    mods {
        create(modId) {
            // Tell Loom about each source set used by your mod here. This ensures that your mod's classes are properly transformed by Loader.
            sourceSet(sourceSets["main"])
            sourceSet(project(":Sparkweave-Common").sourceSets["main"])
        }

        create("${modId}_testmod") {
            sourceSet(sourceSets["testmod"])
            sourceSet(project(":Sparkweave-Common").sourceSets["testmod"])
        }
    }

    accessWidenerPath.set(file("src/main/resources/${modId}.classtweaker"))

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

        create("datagen") {
            client()
            displayName = "Fabric TestmodData"

            sourceSet = "testmod"

            systemProperties.put("fabric-api.datagen", "true")
            systemProperties.put("fabric-api.datagen.strict-validation", "true") // '--all' sets '--validate' to true as well
            systemProperties.put("fabric-api.datagen.output-dir", file("src/testmod/generated").absolutePath)
            systemProperties.put("sparkweave.datagen.mods", "${modId}, ${modId}_testmod")
            runDirectory = file("build/datagen")
        }

        configureEach {
            appendProjectPathToDisplayName = false
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

val generateDatagenDir = tasks.register("generateDatagenDir") {
    description = "Creates the data generation output directory"
    inputs.dir(project.layout.buildDirectory.dir("datagen"))

    doFirst {
        inputs.files.forEach { it.mkdirs() }
    }
}

tasks.named("ideaSyncTask").configure { dependsOn(generateDatagenDir) }
tasks.named("runDatagen").configure { dependsOn(generateDatagenDir) }
sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
