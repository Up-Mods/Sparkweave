package dev.upcraft.gradle.multiloader

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.util.jar.Attributes
import java.util.jar.Manifest
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
abstract class BuildTimeManifestTask @Inject constructor(
    @Inject private val layout: ProjectLayout
) : DefaultTask() {

    @get:Input
    abstract val buildTime: Property<Instant>

    @get:OutputFile
    abstract val manifestPath: Property<String>

    init {
        manifestPath.convention("build/tmp/timestamp.MF")
        this.onlyIf("No build time provided to task") { buildTime.isPresent }
    }

    @TaskAction
    fun run() {
        val mf = Manifest()
        mf.mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
        val timestamp = buildTime.get()
        mf.mainAttributes[Attributes.Name("Implementation-Timestamp")] = timestamp.toString()
        mf.mainAttributes[Attributes.Name("Timestamp")] = timestamp.toEpochMilliseconds().toString()

        layout.projectDirectory.file(manifestPath).get().asFile.outputStream().use { mf.write(it) }
    }
}
