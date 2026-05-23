@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-common")
    id("net.fabricmc.fabric-loom-companion")
    id("net.neoforged.moddev")
}

neoForge.neoFormVersion = libs.versions.neoform.get()

dependencies {
    compileOnly(libs.bundles.mixin)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.autoservice.annotations)

    compileOnly(libs.appdirs)

//	compileOnly(libs.resourcefulconfig)
    compileOnly(variantOf(libs.emi) {
        classifier("api")
    })

    testmodCompileOnly(libs.autoservice.annotations)
    testmodImplementation(sourceSets["main"].output)

    testCompileOnly(libs.junit.api)
    testCompileOnly(libs.neoforge.testframework)
}

neoForge {
    parchment {
        minecraftVersion = libs.versions.parchment.minecraft.get()
        mappingsVersion = libs.versions.parchment.mappings.get()
    }

    accessTransformers {
        val atFile = file("src/main/resources/META-INF/accesstransformer.cfg")
        from(atFile)
        publish(atFile)
    }
    validateAccessTransformers.set(true)

    interfaceInjectionData {
        val interfacesFile = file("src/main/resources/META-INF/interfaces.json")
        from(interfacesFile)
        publish(interfacesFile)
    }

    addModdingDependenciesTo(sourceSets["test"])
}

val commonJava by configurations.consumable("commonJava")
val commonResources by configurations.consumable("commonResources")

val testmodCommonJava by configurations.consumable("testmodCommonJava")
val testmodCommonResources by configurations.consumable("testmodCommonResources")

artifacts {
    add(commonJava.name, sourceSets["main"].java.sourceDirectories.singleFile)
    add(commonResources.name, sourceSets["main"].resources.sourceDirectories.singleFile)

    add(testmodCommonJava.name, sourceSets["testmod"].java.sourceDirectories.singleFile)
    add(testmodCommonResources.name, sourceSets["testmod"].resources.sourceDirectories.singleFile)
}
