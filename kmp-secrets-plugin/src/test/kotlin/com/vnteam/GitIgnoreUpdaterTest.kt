package com.vnteam

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class GitIgnoreUpdaterTest {

    @Test
    fun `given no gitignore file when addToGitIgnore is called then create file with auto-generated text`() {
        val project: Project = ProjectBuilder.builder().build()
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE)

        if (gitignoreFile.exists()) gitignoreFile.delete()

        val configFile = File(project.projectDir, "src/commonMain/kotlin/secrets/Secrets.kt")
        configFile.parentFile.mkdirs()
        configFile.writeText("// test content")

        val updater = GitIgnoreUpdater(project)
        updater.addToGitIgnore(configFile)

        assertTrue(gitignoreFile.exists(), "The .gitignore should be created if it doesn't exist")

        val content = gitignoreFile.readText()
        assertTrue(content.contains(Constants.GITIGNORE_AUTO_GENERATED), "Should contain the auto-generated comment")
        val relativePath = configFile.relativeTo(project.projectDir).path.replace("\\", "/")
        assertTrue(content.contains(relativePath), "Should contain the relative path of config file")
    }

    @Test
    fun `given existing gitignore without path when addToGitIgnore is called then append path`() {
        val project: Project = ProjectBuilder.builder().build()
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE)
        gitignoreFile.writeText("# Existing .gitignore\n")

        val configFile = File(project.projectDir, "src/commonMain/kotlin/secrets/Secrets.kt")
        configFile.parentFile.mkdirs()
        configFile.writeText("// test content")

        val updater = GitIgnoreUpdater(project)
        updater.addToGitIgnore(configFile)

        val content = gitignoreFile.readText()
        val relativePath = configFile.relativeTo(project.projectDir).path.replace("\\", "/")
        assertTrue(content.contains(relativePath), "Should contain the new relative path appended")
    }

    @Test
    fun `given existing gitignore with path when addToGitIgnore is called then do nothing`() {
        val project: Project = ProjectBuilder.builder().build()
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE)

        val relativePath = "src/commonMain/kotlin/secrets/Secrets.kt"
        gitignoreFile.writeText(
            """
            # Existing .gitignore
            $relativePath
            """.trimIndent()
        )

        val configFile = File(project.projectDir, relativePath).apply {
            parentFile.mkdirs()
            writeText("// test content")
        }

        val updater = GitIgnoreUpdater(project)
        updater.addToGitIgnore(configFile)

        val contentAfter = gitignoreFile.readText()

        val linesAfter = contentAfter.lines()
        assertTrue(
            linesAfter.size == 2,
            "Should not append path if it's already present"
        )
    }
}