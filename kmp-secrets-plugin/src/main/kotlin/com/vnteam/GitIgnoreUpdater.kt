package com.vnteam

import org.gradle.api.Project
import java.io.File

class GitIgnoreUpdater(private val project: Project) {

    fun addToGitIgnore(configFile: File) {
        val gitIgnoreFile = project.file(Constants.GITIGNORE_FILE)
        val relativePath = configFile.relativeTo(project.projectDir).path.replace("\\", "/")

        if (!gitIgnoreFile.exists()) {
            println(Constants.GITIGNORE_NOT_FOUND)
            gitIgnoreFile.writeText("${Constants.GITIGNORE_AUTO_GENERATED}\n$relativePath\n")
        } else {
            val gitIgnoreContent = gitIgnoreFile.readText()
            if (!gitIgnoreContent.contains(relativePath)) {
                println(Constants.GITIGNORE_ADDING_ENTRY + relativePath)
                gitIgnoreFile.appendText("\n$relativePath\n")
            } else {
                println(Constants.GITIGNORE_ALREADY_IGNORED)
            }
        }
    }
}
