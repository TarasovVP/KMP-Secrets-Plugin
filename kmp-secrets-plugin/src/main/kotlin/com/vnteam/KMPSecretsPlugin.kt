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
            doFirst {
                val modulePropertiesFile = project.file("local.properties")
                val globalPropertiesFile = project.rootProject.file("local.properties")

                val propertiesFile = when {
                    modulePropertiesFile.exists() -> modulePropertiesFile
                    globalPropertiesFile.exists() -> globalPropertiesFile
                    else -> throw RuntimeException("❌ No local.properties found for module: ${project.name}")
                }
                val kotlinSrcDir = File(extension.outputDir)
                val configDir = File("$kotlinSrcDir/secrets")
                val configFile = File("$configDir/Secrets.kt")

                if (!propertiesFile.exists()) {
                    throw RuntimeException("local.properties file not found at ${propertiesFile.absolutePath}")
                }

                val properties = Properties().apply {
                    load(propertiesFile.inputStream())
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
                    appendLine("object Properties {")

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

                val gitIgnoreFile = project.file(".gitignore")
                val relativePath = configFile.relativeTo(project.projectDir).path.replace("\\", "/")

                if (!gitIgnoreFile.exists()) {
                    println("🛠️ .gitignore not found, creating a new one...")
                    gitIgnoreFile.writeText("# Auto-generated .gitignore\n$relativePath\n")
                } else {
                    val gitIgnoreContent = gitIgnoreFile.readText()
                    if (!gitIgnoreContent.contains(relativePath)) {
                        println("🛠️ Adding $relativePath to .gitignore...")
                        gitIgnoreFile.appendText("\n$relativePath\n")
                    } else {
                        println("✅ $relativePath is already ignored in .gitignore")
                    }
                }
            }
        }

        project.afterEvaluate {
            project.tasks.findByName(extension.triggerTask)?.dependsOn("generateSecrets")
        }
    }
}