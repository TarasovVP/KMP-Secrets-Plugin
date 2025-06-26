package com.vnteam

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File

class KMPSecretsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(
                Constants.SECRETS_EXTENSION_NAME,
                SecretsPluginExtension::class.java,
                project
            )

        project.plugins.withId(Constants.KOTLIN_MULTIPLATFORM) {
            val kmpExtension = project.extensions
                .getByType(KotlinMultiplatformExtension::class.java)

            configureSecretsGeneration(project, kmpExtension, extension)
        }

    }

    private fun configureSecretsGeneration(
        project: Project,
        kmp: KotlinMultiplatformExtension,
        ext: SecretsPluginExtension
    ) {
        kmp.targets.configureEach {
            val targetName = name.replaceFirstChar { it.titlecase() }

            compilations.configureEach {
                val compilationName = name.replaceFirstChar { it.titlecase() }

                val taskName = buildString {
                    append(Constants.GENERATE_SECRETS_TASK)
                    append(targetName)
                    append(compilationName)
                }

                val taskProvider = project.tasks.register<GenerateSecretsTask>(taskName) {
                    propertiesFile.set(findPropertiesFile(project))

                    val outDir = File(ext.outputDir, Constants.SECRETS_PACKAGE_NAME)
                    outputFile.set(File(outDir, Constants.SECRETS_FILE_NAME))

                    projectDir.set(project.layout.projectDirectory)
                }

                compileKotlinTask.dependsOn(taskProvider)
            }
        }
    }

    private fun findPropertiesFile(project: Project): File {
        val moduleProps = project.file(Constants.LOCAL_PROPERTIES_FILE)
        val rootProps = project.rootProject.file(Constants.LOCAL_PROPERTIES_FILE)
        return when {
            moduleProps.exists() -> moduleProps
            rootProps.exists() -> rootProps
            else -> throw GradleException(Constants.ERROR_NO_PROPERTIES_FOUND + project.name)
        }
    }
}
