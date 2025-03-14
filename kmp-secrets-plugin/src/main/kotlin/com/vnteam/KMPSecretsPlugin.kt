package com.vnteam

import org.gradle.api.Plugin
import org.gradle.api.Project

class KMPSecretsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(Constants.SECRETS_EXTENSION_NAME, SecretsPluginExtension::class.java, project)

        project.tasks.register(Constants.GENERATE_SECRETS_TASK) {
            doFirst {
                val generator = SecretsGenerator(project, extension)
                val generatedFile = generator.generateSecrets()

                val gitIgnoreUpdater = GitIgnoreUpdater(project)
                gitIgnoreUpdater.addToGitIgnore(generatedFile)
            }
        }

        project.afterEvaluate {
            project.tasks.findByName(extension.triggerTask)?.dependsOn(Constants.GENERATE_SECRETS_TASK)
        }
    }
}
