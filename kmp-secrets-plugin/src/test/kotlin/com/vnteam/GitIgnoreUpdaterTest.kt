package com.vnteam

import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GitIgnoreUpdaterTest {

    @Test
    fun `given no gitignore file when addToGitIgnore is called then create file with auto-generated text`() {
        val project = ProjectBuilder.builder().build()
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE)
        if (gitignoreFile.exists()) gitignoreFile.delete()

        val configFile = File(project.projectDir, "src/commonMain/kotlin/secrets/Secrets.kt").apply {
            parentFile.mkdirs()
            writeText("// test content")
        }

        GitIgnoreUpdater(gitignoreFile).addToGitIgnore(configFile)

        assertTrue(gitignoreFile.exists())
        val content = gitignoreFile.readText()
        val relativePath = configFile.relativeTo(project.projectDir).invariantSeparatorsPath
        assertTrue(content.contains(Constants.GITIGNORE_AUTO_GENERATED))
        assertTrue(content.contains(relativePath))
    }

    @Test
    fun `given existing gitignore without path when addToGitIgnore is called then append path`() {
        val project = ProjectBuilder.builder().build()
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE).apply {
            writeText("# Existing .gitignore\n")
        }

        val configFile = File(project.projectDir, "src/commonMain/kotlin/secrets/Secrets.kt").apply {
            parentFile.mkdirs()
            writeText("// test content")
        }

        GitIgnoreUpdater(gitignoreFile).addToGitIgnore(configFile)

        val relativePath = configFile.relativeTo(project.projectDir).invariantSeparatorsPath
        assertTrue(gitignoreFile.readText().contains(relativePath))
    }

    @Test
    fun `given existing gitignore with path when addToGitIgnore is called then do nothing`() {
        val project = ProjectBuilder.builder().build()
        val relativePath = "src/commonMain/kotlin/secrets/Secrets.kt"
        val gitignoreFile = File(project.projectDir, Constants.GITIGNORE_FILE).apply {
            writeText(
                """
                # Existing .gitignore
                $relativePath
                """.trimIndent()
            )
        }

        val configFile = File(project.projectDir, relativePath).apply {
            parentFile.mkdirs()
            writeText("// test content")
        }

        GitIgnoreUpdater(gitignoreFile).addToGitIgnore(configFile)

        assertEquals(2, gitignoreFile.readLines().size)
    }
}