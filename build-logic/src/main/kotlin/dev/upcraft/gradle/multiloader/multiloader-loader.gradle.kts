package dev.upcraft.gradle.multiloader

plugins {
    id("dev.upcraft.gradle.multiloader.multiloader-common")
}

val commonJava by configurations.resolvable("commonJava")
val commonResources by configurations.resolvable("commonResources")

val testmodCommonJava by configurations.resolvable("testmodCommonJava")
val testmodCommonResources by configurations.resolvable("testmodCommonResources")

dependencies {
    compileOnly(project(":${rootProject.name}-Common")) {
        forLoader("common")
        capabilities {
            requireCapability("${project.group}:${rootProject.name}")
        }
    }

    commonJava(project(":${rootProject.name}-Common", "commonJava"))
    commonResources(project(":${rootProject.name}-Common", "commonResources"))

    testmodCommonJava(project(":${rootProject.name}-Common", "testmodCommonJava"))
    testmodCommonResources(project(":${rootProject.name}-Common", "testmodCommonResources"))
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
