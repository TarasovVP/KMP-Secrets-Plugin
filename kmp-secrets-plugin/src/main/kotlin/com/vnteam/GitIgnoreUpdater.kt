package com.vnteam

import java.io.File

class GitIgnoreUpdater(private val gitIgnoreFile: File) {

    fun addToGitIgnore(configFile: File) {
        val projectDir = gitIgnoreFile.parentFile
        val relative = configFile
            .relativeTo(projectDir)
            .invariantSeparatorsPath

        if (!gitIgnoreFile.exists()) {
            gitIgnoreFile.writeText("${Constants.GITIGNORE_AUTO_GENERATED}\n$relative\n")
        } else if (!gitIgnoreFile.readText().contains(relative)) {
            gitIgnoreFile.appendText("\n$relative\n")
        }
    }
}
