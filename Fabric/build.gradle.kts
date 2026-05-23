@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions


plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("net.fabricmc.fabric-loom-remap")
}
applyMcGradleConventions("fabric")

val modId: String = property("mod_id").toString()

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
        configureEach {
            ideConfigGenerated(true)
            property("sparkweave.debug", "true")
            property("mixin.debug", "true")
        }

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
            configName = "Fabric Testmod Client"
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
            configName = "Fabric Data Generation"

            source(sourceSets["testmod"])

            property("fabric-api.datagen")
            property("fabric-api.datagen.strict-validation", "true") // '--all' sets '--validate' to true as well
            property("fabric-api.datagen.output-dir", file("src/testmod/generated").absolutePath)
            property("sparkweave.datagen.mods", "${modId}, ${modId}_testmod")
            runDir("build/datagen")
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

tasks.named("ideaSyncTask").configure { finalizedBy(generateDatagenDir) }
tasks.named("runDatagen").configure { dependsOn(generateDatagenDir) }
sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
