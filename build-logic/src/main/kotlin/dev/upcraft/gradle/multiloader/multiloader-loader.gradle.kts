@file:Suppress("UnstableApiUsage")

package dev.upcraft.gradle.multiloader

plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-common")
}

val commonJavaDep = configurations.dependencyScope("commonJavaDep")
val commonJava = configurations.resolvable("commonJava") { extendsFrom(commonJavaDep) }

val commonResourcesDep = configurations.dependencyScope("commonResourcesDep")
val commonResources = configurations.resolvable("commonResources") { extendsFrom(commonResourcesDep) }

val testmodCommonJavaDep = configurations.dependencyScope("testmodCommonJavaDep")
val testmodCommonJava = configurations.resolvable("testmodCommonJava") { extendsFrom(testmodCommonJavaDep) }
val testmodCommonResourcesDep = configurations.dependencyScope("testmodCommonResourcesDep")
val testmodCommonResources = configurations.resolvable("testmodCommonResources") { extendsFrom(testmodCommonResourcesDep) }

dependencies {
    compileOnly(project(":${rootProject.name}-Common")) {
        attributes { loaderAttribute("common") }
    }
    "testmodCompileOnly"(project(":${rootProject.name}-Common")) {
        attributes { loaderAttribute("common") }
        capabilities { requireFeature("testmod") }
    }

    commonJavaDep(project(":${rootProject.name}-Common", "commonJava"))
    commonResourcesDep(project(":${rootProject.name}-Common", "commonResources"))

    testmodCommonJavaDep(project(":${rootProject.name}-Common", "testmodCommonJava"))
    testmodCommonResourcesDep(project(":${rootProject.name}-Common", "testmodCommonResources"))
}

tasks.named<JavaCompile>("compileJava").configure {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<ProcessResources>("processResources").configure {
    dependsOn(commonResources)
    from(commonResources)
}

tasks.named<Javadoc>("javadoc").configure {
    dependsOn(commonJava)
    source(commonJava)
}

tasks.named<Jar>("sourcesJar").configure {
    dependsOn(commonJava, commonResources)
    from(commonJava, commonResources)
}
