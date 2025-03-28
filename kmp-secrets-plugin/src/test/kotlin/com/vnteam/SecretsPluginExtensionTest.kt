package com.vnteam

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.assertEquals
import kotlin.test.Test

class SecretsPluginExtensionTest {

    @Test
    fun `given a Gradle project when extension is created then outputDir has default value`() {
        val project: Project = ProjectBuilder.builder().build()

        val extension = SecretsPluginExtension(project)

        val expectedPath = "${project.projectDir}/${Constants.DEFAULT_OUTPUT_DIR}"
        assertEquals(expectedPath, extension.outputDir,
            "By default, outputDir should match 'projectDir + DEFAULT_OUTPUT_DIR'")
    }
}