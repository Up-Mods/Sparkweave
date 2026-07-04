@file:Suppress("UnstableApiUsage")

import dev.upcraft.gradle.multiloader.applyMcGradleConventions

plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-common")
    id("net.fabricmc.fabric-loom-companion")
    id("net.neoforged.moddev")
}
applyMcGradleConventions("common")

neoForge.neoFormVersion = libs.versions.neoform.get()

dependencies {
    compileOnly(libs.bundles.mixin)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.autoservice.annotations)

    compileOnly(libs.appdirs)

//	compileOnly(libs.resourcefulconfig)
    compileOnly(libs.jei.api)

    testmodCompileOnly(libs.autoservice.annotations)
    testmodImplementation(sourceSets["main"].output)

    testCompileOnly(libs.junit.api)
    testCompileOnly(libs.neoforge.testframework)
}

neoForge {
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

val commonJava = configurations.consumable("commonJava")
val commonResources = configurations.consumable("commonResources")

val testmodCommonResources = configurations.consumable("testmodCommonResources")
val testmodCommonJava = configurations.consumable("testmodCommonJava")

artifacts {
    add(commonJava.name, sourceSets["main"].java.sourceDirectories.singleFile)
    add(commonResources.name, sourceSets["main"].resources.sourceDirectories.singleFile)

    add(testmodCommonJava.name, sourceSets["testmod"].java.sourceDirectories.singleFile)
    add(testmodCommonResources.name, sourceSets["testmod"].resources.sourceDirectories.singleFile)
}
