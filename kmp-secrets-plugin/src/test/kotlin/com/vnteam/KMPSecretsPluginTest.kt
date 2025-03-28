package com.vnteam

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMPSecretsPluginTest {

    @Test
    fun `given no KMP plugin when plugin is applied then extension is created and no generateSecrets tasks exist`() {
        val project: Project = ProjectBuilder.builder().build()

        project.pluginManager.apply("io.github.tarasovvp.kmp-secrets-plugin")

        val extension = project.extensions.findByName(Constants.SECRETS_EXTENSION_NAME)
        assertNotNull(extension, "SecretsPluginExtension should be created even without the KMP plugin")

        val allTasks = project.tasks.names
        assertTrue(
            allTasks.none { it.contains("generateSecrets", ignoreCase = true) },
            "Without KMP plugin, generateSecrets tasks should not appear"
        )
    }

    @Test
    fun `given KMP plugin when plugin is applied then extension is created and generateSecrets tasks exist`() {
        val project: Project = ProjectBuilder.builder().build()

        project.pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        project.pluginManager.apply("io.github.tarasovvp.kmp-secrets-plugin")

        val extension = project.extensions.findByName(Constants.SECRETS_EXTENSION_NAME)
        assertNotNull(extension, "SecretsPluginExtension should be created")

        val allTasks = project.tasks.names
        assertTrue(
            allTasks.any { it.contains("generateSecrets", ignoreCase = true) },
            "With KMP plugin, generateSecrets tasks should appear"
        )
    }
}
