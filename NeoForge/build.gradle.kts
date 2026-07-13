@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions
import java.util.*

plugins {
	id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("net.neoforged.moddev")
}
applyMcGradleConventions("neoforge")

val modId = providers.gradleProperty("mod_id").get()

// need this before dependencies because it configures the plugin and creates additionalRuntimeClasspath configuration
neoForge.version = libs.versions.neoforge.get()

val localRuntime = configurations.dependencyScope("localRuntime")
configurations.runtimeClasspath.configure { extendsFrom(localRuntime) }

dependencies {
	interfaceInjectionData(project(":${rootProject.name}-Common"))
	accessTransformers(project(":${rootProject.name}-Common"))

//	implementation(libs.resourcefulconfig.neoforge)

	implementation(libs.appdirs)
	"jarJar"(libs.appdirs) {
		isTransitive = false
	}

    compileOnly(libs.jei.neoforge.api)
    localRuntime(libs.jei.neoforge)

	testImplementation(libs.neoforge.testframework)
}

neoForge {
	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// mostly optional in a single mod project
		// but multi mod projects should define one per mod
        register(modId) {
			sourceSet(sourceSets["main"])
		}

        register("${modId}_testmod") {
			sourceSet(sourceSets["testmod"])
		}
	}

	unitTest {
		enable()

		testedMod = mods[modId]
		loadedMods = listOf(mods[modId])
	}

    runs {
        register("client") {
            client()
            devLogin = true
            gameDirectory = file("run/client")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            sourceSet = sourceSets["main"]
            loadedMods = listOf(mods[modId])
        }

        register("server") {
            server()
            gameDirectory = file("run/server")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            sourceSet = sourceSets["main"]
            loadedMods = listOf(mods[modId])

            programArgument("--nogui")
        }

        register("testmodClient") {
            client()
            devLogin = true
            gameDirectory = file("run/testmod_client")
            systemProperty("neoforge.enabledGameTestNamespaces", "${modId}_testmod")

            sourceSet = sourceSets["testmod"]
            loadedMods = listOf(mods[modId], mods["${modId}_testmod"])
        }

        register("testmodData") {
            clientData()
            gameDirectory = file("run/testmod_data")

            programArguments.addAll(
                "--mod", modId,
                "--mod", "${modId}_testmod",
                "--all",
                "--flat",
                "--output", file("src/testmod/generated").absolutePath,
                "--existing", file("src/testmod/resources").absolutePath
            )
            sourceSet = sourceSets["testmod"]
            loadedMods = listOf(mods[modId], mods["${modId}_testmod"])
        }

        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("terminal.ansi", "true")
            systemProperty("sparkweave.debug", "true")

            ideName = "NeoForge ${name.replaceFirstChar { it.titlecase(Locale.ROOT) }}"
        }
    }
}

sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
