plugins {
    id("dev.upcraft.gradle.multiloader")
    id("dev.upcraft.gradle.custom")
    id("net.fabricmc.fabric-loom")
}

val modID: String = providers.gradleProperty("mod_id").get()

multiLoader {
    javaVersion = libs.versions.java.map { it.toInt() }
    minecraftVersion = libs.versions.minecraft

    loader = "fabric"

    withTestmod()
    setCommonProject(":${rootProject.name}-Common")
    applyMetadataReplacements(listOf("pack.mcmeta", "*.mixins.json", "fabric.mod.json"), mapOf(
        "fabric_api_version" to libs.versions.fabric.api,
        "fabric_loader_version" to libs.versions.fabric.loader
    ))
}

dependencies {
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

    runs {
        fabricApi.configureDataGeneration {
            outputDirectory = file("src/testmod/generated")
            addToResources = false

            client = true
            modId = modID
            strictValidation = true // neoforge '--all' sets '--validate' to true as well
        }

        named("datagen") {
            displayName = "Fabric TestmodData"

            sourceSet = "testmod"

            systemProperties.put("sparkweave.datagen.mods", "${modID}_testmod")
        }
    }
}

sourceSets["testmod"].resources { srcDir("src/testmod/generated") }
