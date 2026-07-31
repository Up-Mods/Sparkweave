plugins {
    id("dev.upcraft.gradle.multiloader")
    id("dev.upcraft.gradle.custom")
    id("net.fabricmc.fabric-loom-companion")
    id("net.neoforged.moddev")
}

neoForge.neoFormVersion = libs.versions.neoform.get()

val modID = providers.gradleProperty("mod_id").get()

multiLoader {
    javaVersion = libs.versions.java.map { it.toInt() }
    minecraftVersion = libs.versions.minecraft

    withTestmod()
    applyMetadataReplacements(listOf("pack.mcmeta", "*.mixins.json"))
}

dependencies {
    compileOnly(libs.appdirs)

//	compileOnly(libs.resourcefulconfig)
    compileOnly(libs.jei.api)

    testCompileOnly(libs.neoforge.testframework)
}

neoForge {
    mods {
        register(modID) {
            sourceSet(sourceSets["main"])
        }
        register("${modID}_testmod") {
            sourceSet(sourceSets["testmod"])
        }
    }
}
