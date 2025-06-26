package com.vnteam

import java.io.File

class GitIgnoreUpdater(private val projectDir: File) {

    fun addToGitIgnore(configFile: File) {
        val gitIgnoreFile = File(projectDir, Constants.GITIGNORE_FILE)

        val relativePath = configFile.relativeTo(projectDir).path.replace("\\", "/")
        if (!gitIgnoreFile.exists()) {
            gitIgnoreFile.writeText("${Constants.GITIGNORE_AUTO_GENERATED}\n$relativePath\n")
        } else {
            val gitIgnoreContent = gitIgnoreFile.readText()
            if (!gitIgnoreContent.contains(relativePath)) {
                gitIgnoreFile.appendText("\n$relativePath\n")
            }
        }
    }
}
