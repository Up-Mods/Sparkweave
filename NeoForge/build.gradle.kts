plugins {
	id("dev.upcraft.gradle.multiloader.multiloader-loader")
    id("net.neoforged.moddev")
}

val modId: String = property("mod_id").toString()

// need this before dependencies because it configures the plugin and creates additionalRuntimeClasspath configuration
neoForge.version = libs.versions.neoforge.get()

val localRuntime by configurations.register("localRuntime") {

}
configurations.runtimeClasspath.configure {
    extendsFrom(localRuntime)
}

dependencies {
	interfaceInjectionData(project(":Sparkweave-Common"))
	accessTransformers(project(":Sparkweave-Common"))
	compileOnly(libs.autoservice.annotations)
	annotationProcessor(libs.autoservice)

	testmodImplementation(sourceSets["main"].output)
	testmodCompileOnly(libs.autoservice.annotations)
	testmodAnnotationProcessor(libs.autoservice)

//	implementation(libs.resourcefulconfig.neoforge)

	implementation(libs.appdirs)
	"additionalRuntimeClasspath"(libs.appdirs)
	"jarJar"(libs.appdirs) {
		isTransitive = false
	}

	compileOnly(variantOf(libs.emi.neoforge) {
		classifier("api")
	})
	localRuntime(libs.emi.neoforge)

	testImplementation(libs.junit.api)
	testImplementation(libs.neoforge.testframework)
	testRuntimeOnly(libs.junit.launcher)
	testRuntimeOnly(libs.junit.engine)
}

neoForge {
	parchment {
		minecraftVersion = libs.versions.parchment.minecraft.get()
		mappingsVersion = libs.versions.parchment.mappings.get()
	}

	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// mostly optional in a single mod project
		// but multi mod projects should define one per mod
        register(modId) {
			sourceSet(sourceSets["main"])
			sourceSet(project(":Sparkweave-Common").sourceSets["main"])
		}

        register("${modId}_testmod") {
			sourceSet(sourceSets["testmod"])
			sourceSet(project(":Sparkweave-Common").sourceSets["testmod"])
		}
	}

	unitTest {
		enable()

		testedMod = mods[modId]
		loadedMods = listOf(mods[modId])
	}

    runs {
        configureEach {
			logLevel = org.slf4j.event.Level.DEBUG
			systemProperty("forge.logging.markers", "REGISTRIES")
			systemProperty("sparkweave.debug", "true")
		}

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
            data() // TODO set to clientData() in 26.1
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
    }
}

sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
