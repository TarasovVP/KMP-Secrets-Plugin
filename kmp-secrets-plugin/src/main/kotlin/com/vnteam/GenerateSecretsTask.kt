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

    @get:OutputFile
    @get:Optional
    abstract val gitIgnoreFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val generated = SecretsGenerator()
            .generateSecrets(
                propsFile   = propertiesFile.get().asFile,
                outputFile  = outputFile.get().asFile
            )

        GitIgnoreUpdater(gitIgnoreFile.get().asFile)
            .addToGitIgnore(generated)
    }
}