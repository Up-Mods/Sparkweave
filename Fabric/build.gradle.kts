@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions
import net.fabricmc.loom.task.LoomTasks
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings


plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("org.jetbrains.gradle.plugin.idea-ext")
    id("net.fabricmc.fabric-loom-remap")
}
applyMcGradleConventions("fabric")

val modId: String = providers.gradleProperty("mod_id").get()

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchment.minecraft.get()}:${libs.versions.parchment.mappings.get()}@zip")
    })

    compileOnly(libs.autoservice.annotations)
    annotationProcessor(libs.autoservice)

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    testmodImplementation(sourceSets["main"].output)
    testmodCompileOnly(libs.autoservice.annotations)
    testmodAnnotationProcessor(libs.autoservice)

//	modImplementation(libs.resourcefulconfig.fabric) {
//      isTransitive = false
//	}

    implementation(libs.appdirs)
    include(libs.appdirs)

    modCompileOnly(variantOf(libs.emi.fabric) { classifier("api") }) {
        isTransitive = false
    }
    modLocalRuntime(libs.emi.fabric) {
        isTransitive = false
    }

    modCompileOnly(libs.modmenu.fabric) {
        isTransitive = false
    }
    modLocalRuntime(libs.modmenu.fabric) {
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

    accessWidenerPath.set(file("src/main/resources/${modId}.accesswidener"))

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            runDir("run")

            if (project.hasProperty("mc_uuid")) {
                programArg("--uuid=${project.findProperty("mc_uuid")}")
            }

            if (project.hasProperty("mc_username")) {
                programArg("--username=${project.findProperty("mc_username")}")
            }
        }

        create("testmodClient") {
            client()
            configName = "Fabric TestmodClient"
            runDir("run/testmod")
            source(sourceSets["testmod"])

            if (project.hasProperty("mc_uuid")) {
                programArg("--uuid=${project.findProperty("mc_uuid")}")
            }

            if (project.hasProperty("mc_username")) {
                programArg("--username=${project.findProperty("mc_username")}")
            }
        }

        named("server") {
            server()
            configName = "Fabric Server"
            runDir("run/server")
        }

        create("datagen") {
            client()
            configName = "Fabric TestmodData"

            source(sourceSets["testmod"])

            property("fabric-api.datagen")
            property("fabric-api.datagen.strict-validation", "true") // '--all' sets '--validate' to true as well
            property("fabric-api.datagen.output-dir", file("src/testmod/generated").absolutePath)
            property("sparkweave.datagen.mods", "${modId}, ${modId}_testmod")
            runDir("build/datagen")
        }

        configureEach {
            appendProjectPathToConfigName = false
            property("sparkweave.debug", "true")
            property("mixin.debug", "true")

            makeRunDir()

            // register as Gradle runs instead of IDEA runs
            // https://github.com/FabricMC/fabric-loom/issues/1349
            isIdeConfigGenerated = false
            rootProject.idea.project.settings.runConfigurations.create<org.jetbrains.gradle.ext.Gradle>(configName) {
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
