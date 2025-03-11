package com.vnteam

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.Properties


class KMPSecretsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create("secretsConfig", SecretsPluginExtension::class.java, project)

        project.tasks.register("generateSecrets") {
            doLast {
                val localProperties = File(extension.propertiesFile)
                val kotlinSrcDir = File(extension.outputDir)
                val configDir = File("$kotlinSrcDir/secrets")
                val configFile = File("$configDir/Secrets.kt")

                if (!localProperties.exists()) {
                    throw RuntimeException("local.properties file not found at ${localProperties.absolutePath}")
                }

                val properties = Properties().apply {
                    load(localProperties.inputStream())
                }

                if (!configDir.exists()) {
                    configDir.mkdirs()
                }

                fun isValidKey(key: String): Boolean {
                    return key.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))
                }

                val packagePath = configDir.relativeTo(kotlinSrcDir)
                val packageName = packagePath.toString().replace("/", ".").replace("\\", ".")

                val configContent = buildString {
                    appendLine("package $packageName")
                    appendLine()
                    appendLine("object Secrets {")

                    properties.forEach { (keyAny, value) ->
                        val key = keyAny.toString()
                        if (isValidKey(key)) {
                            appendLine("    val $key = \"$value\"")
                        }
                    }

                    appendLine("}")
                }

                configFile.writeText(configContent)
                println("✅ Secrets.kt successfully generated at ${configFile.absolutePath}")
            }
        }

        project.afterEvaluate {
            project.tasks.findByName(extension.triggerTask)?.dependsOn("generateSecrets")
        }
    }
}