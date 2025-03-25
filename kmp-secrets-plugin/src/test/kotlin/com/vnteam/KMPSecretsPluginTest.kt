package com.vnteam

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class KMPSecretsPluginTest {

    @Test
    fun `plugin creates secrets file`() {
        val projectDir = File("build/functionalTest").apply { mkdirs() }
        val buildFile = File(projectDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("io.github.tarasovvp.kmp-secrets-plugin")
            }
        """.trimIndent()
        )

        val localProperties = File(projectDir, "local.properties")
        localProperties.writeText(
            """
            AUTH_API_KEY=test_api_key
            SOME_OTHER_SECRET=test_secret_value
        """.trimIndent()
        )
        File(projectDir, "settings.gradle.kts").writeText("rootProject.name = \"functionalTest\"")
        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("generateSecrets")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateSecrets")?.outcome)

        val generatedFile = File(projectDir, "src/commonMain/kotlin/secrets/Secrets.kt")
        assert(generatedFile.exists()) { "Secrets.kt file was not created" }
    }
}