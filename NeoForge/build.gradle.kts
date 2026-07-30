plugins {
    id("dev.upcraft.gradle.multiloader")
    id("dev.upcraft.gradle.custom")
    id("net.neoforged.moddev")
}

val modID = providers.gradleProperty("mod_id").get()

// need this before dependencies because it configures the plugin and creates additionalRuntimeClasspath configuration
neoForge.version = libs.versions.neoforge.get()

multiLoader {
    javaVersion = libs.versions.java.map { it.toInt() }
    minecraftVersion = libs.versions.minecraft

    loader = "neoforge"

    withTestmod()
    setCommonProject(":${rootProject.name}-Common")
    applyMetadataReplacements(listOf("pack.mcmeta", "*.mixins.json", "META-INF/neoforge.mods.toml"), mapOf(
        "neoforge_version" to libs.versions.neoforge
    ))
}

dependencies {
//	implementation(libs.resourcefulconfig.neoforge)

	implementation(libs.appdirs)
	"jarJar"(libs.appdirs) {
		isTransitive = false
	}

    compileOnly(libs.jei.neoforge.api)
    "localRuntime"(libs.jei.neoforge)

	testImplementation(libs.neoforge.testframework)
}

neoForge {
	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// mostly optional in a single mod project
		// but multi mod projects should define one per mod
        register(modID) {
            sourceSet(sourceSets["main"])
        }
        register("${modID}_testmod") {
            sourceSet(sourceSets["testmod"])
        }
    }

    runs {
        register("testmodData") {
            clientData()
            gameDirectory = file("run/testmod_data")

            systemProperty("sparkweave.datagen.mods", "${modID}_testmod")

            programArguments.addAll(
                "--mod", modID,
                "--all",
                "--flat",
                "--output", file("src/testmod/generated").absolutePath,
                "--existing", file("src/testmod/resources").absolutePath
            )
            sourceSet = sourceSets["testmod"]
            loadedMods = listOf(mods[modID], mods["${modID}_testmod"])
        }
    }
}

sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
