package com.vnteam

import org.gradle.api.Plugin
import org.gradle.api.Project

class KMPSecretsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(Constants.SECRETS_EXTENSION_NAME, SecretsPluginExtension::class.java, project)

        val generateSecretsTask = project.tasks.register(Constants.GENERATE_SECRETS_TASK) {
            doFirst {
                println("🔹 KMPSecretsPlugin generateSecrets doFirst")
                val generator = SecretsGenerator(project, extension)
                val generatedFile = generator.generateSecrets()

                val gitIgnoreUpdater = GitIgnoreUpdater(project)
                gitIgnoreUpdater.addToGitIgnore(generatedFile)
            }
        }

        project.afterEvaluate {

            val compileKotlinTasks = project.tasks.names.filter { it.contains("compileKotlin", ignoreCase = true) }
            val firstCompileTask = compileKotlinTasks.firstOrNull()?.let { project.tasks.findByName(it) }

            val androidPreBuildTask = project.tasks.findByName("preBuild")

            val targetTask = firstCompileTask ?: androidPreBuildTask

            if (targetTask != null) {
                targetTask.dependsOn("generateSecretsTask")
                println("🔹 Adding generateSecrets before ${targetTask.name} in module: ${project.name}")
            } else {
                println("⚠️ No matching compileKotlin or preBuild task found in ${project.name}. Secrets may not be generated.")
            }
        }

        /*project.afterEvaluate {
            println("🔹 project.gradle.startParameter.taskNames ${project.gradle.startParameter.taskNames} in module: ${project.name}")
            val firstExecutableTaskName = project.gradle.startParameter.taskNames.firstOrNull { it != Constants.GENERATE_SECRETS_TASK }

            if (!firstExecutableTaskName.isNullOrEmpty()) {
                val firstExecutableTask = project.tasks.findByName(firstExecutableTaskName)

                if (firstExecutableTask != null) {
                    println("🔹 Adding generateSecrets before ${firstExecutableTask.name} in module: ${project.name}")
                    firstExecutableTask.dependsOn(generateSecretsTask)
                }
            } else {
                println("⚠️ No executable tasks found for module: ${project.name}, Secrets.kt may not be generated.")
            }
        }*/


        /*project.gradle.taskGraph.whenReady {
            val firstExecutableTask = allTasks.firstOrNull()

            if (firstExecutableTask != null && firstExecutableTask.name != Constants.GENERATE_SECRETS_TASK) {
                println("🔹 Adding generateSecrets before ${firstExecutableTask.name} in module: ${project.name}")
                firstExecutableTask.dependsOn(generateSecretsTask)
            } else {
                println("⚠️ No executable tasks found for module: ${project.name}, Secrets.kt may not be generated.")
            }
        }*/
        /*val firstTask = project.tasks.firstOrNull()

        if (firstTask != null && firstTask.name != Constants.GENERATE_SECRETS_TASK) {
            println("🔹 Adding generateSecrets before ${firstTask.name} in module: ${project.name}")
        } else {
            println("⚠️ No tasks found for module: ${project.name}, Secrets.kt may not be generated.")
        }
        project.afterEvaluate {
            val foundTask = project.tasks.findByName(firstTask?.name.orEmpty())
            println("🔹 Adding afterEvaluate foundTask $foundTask")
            foundTask?.dependsOn(Constants.GENERATE_SECRETS_TASK)
        }*/
        /*project.tasks.configureEach {
            if (name == "preBuild") {
                println("🔹 Adding generateSecrets before $name in module: ${project.name}")
                dependsOn(generateSecretsTask)
            }
        }*/

        /*project.afterEvaluate {
            println("🔹 afterEvaluate starting for module: ${project.name}")
            project.tasks.findByName("preBuild")?.dependsOn(Constants.GENERATE_SECRETS_TASK)
            val foundTask = project.tasks.any { it.name == "preBuild" }
            println("🔹 Found task is foundTask")
            *//*val firstTask = project.tasks.firstOrNull()

            if (firstTask != null && firstTask.name != Constants.GENERATE_SECRETS_TASK) {
                println("🔹 Adding generateSecrets before firstTask.name ${firstTask.name} in module: ${project.name}")
                project.tasks.findByName("preBuild")?.dependsOn(Constants.GENERATE_SECRETS_TASK)
                //project.tasks.findByName(firstTask.name)?.dependsOn(Constants.GENERATE_SECRETS_TASK)
                //firstTask.dependsOn(generateSecretsTask)
            } else {
                println("⚠️ No tasks found for module: ${project.name}, Secrets.kt may not be generated.")
            }*//*
        }*/

    }
}
