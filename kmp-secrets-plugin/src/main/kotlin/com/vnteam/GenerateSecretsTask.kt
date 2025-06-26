package com.vnteam

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

abstract class GenerateSecretsTask : DefaultTask() {

    @get:InputFile
    abstract val propertiesFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:InputDirectory
    abstract val projectDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val generator = SecretsGenerator()
        val generated = generator.generateSecrets(
            propsFile = propertiesFile.get().asFile,
            outputFile = outputFile.get().asFile
        )
        GitIgnoreUpdater(projectDir = projectDir.get().asFile)
            .addToGitIgnore(generated)
    }
}