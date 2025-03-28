package com.vnteam

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import  kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SecretsGeneratorTest {

    @Test
    fun `given localproperties in module dir when generateSecrets is called then use local properties file`() {
        val project: Project = ProjectBuilder.builder().build()
        val modulePropertiesFile = File(project.projectDir, "local.properties").apply {
            writeText("API_KEY=local_key\nINVALID-KEY=xxx")
        }
        val extension = SecretsPluginExtension(project).apply {
            outputDir = "${project.projectDir}/build/generated" // custom output path
        }

        val generator = SecretsGenerator(project, extension)
        val secretsFile = generator.generateSecrets()

        assertTrue(
            secretsFile.exists(),
            "Secrets.kt file should be created in build/generated/secrets/Secrets.kt"
        )

        val content = secretsFile.readText()
        assertTrue(
            content.contains("val API_KEY = \"local_key\""),
            "Should include valid key from local.properties"
        )
        assertFalse(content.contains("INVALID-KEY"), "Invalid key format should be skipped")
    }

    @Test
    fun `given no localproperties in module but present in root when generateSecrets is called then use root properties file`() {
        val project: Project = ProjectBuilder.builder().build()

        val globalPropertiesFile = File(project.rootProject.projectDir, "local.properties").apply {
            writeText("ROOT_SECRET=root_secret_value")
        }
        val extension = SecretsPluginExtension(project)

        val generator = SecretsGenerator(project, extension)
        val secretsFile = generator.generateSecrets()

        assertTrue(
            secretsFile.exists(),
            "Secrets.kt should be created from rootProject local.properties"
        )
        val content = secretsFile.readText()
        assertTrue(
            content.contains("val ROOT_SECRET = \"root_secret_value\""),
            "Should load property from root local.properties if module file not found"
        )
    }

    @Test
    fun `given no localproperties at all when generateSecrets is called then throw exception`() {
        val project: Project = ProjectBuilder.builder().build()
        val extension = SecretsPluginExtension(project)

        val generator = SecretsGenerator(project, extension)
        val ex = assertFailsWith<RuntimeException> {
            generator.generateSecrets()
        }
        assertTrue(
            ex.message?.contains(Constants.ERROR_NO_PROPERTIES_FOUND) == true,
            "Should throw if no local.properties found in module or root"
        )
    }

    @Test
    fun `given valid propertiesfile when generateSecrets is called then create secrets file with correct package`() {
        val project: Project = ProjectBuilder.builder().build()
        File(project.projectDir, "local.properties").writeText("API_KEY=12345")
        val extension = SecretsPluginExtension(project).apply {
            outputDir = "${project.projectDir}/customSrc"
        }

        val generator = SecretsGenerator(project, extension)
        val secretsFile = generator.generateSecrets()

        val content = secretsFile.readText()

        assertTrue(
            content.contains("package secrets"),
            "Should generate package name 'secrets' inside Secrets.kt"
        )
        assertTrue(
            content.contains("val API_KEY = \"12345\""),
            "Should contain property from local.properties"
        )
        assertEquals(
            File("${project.projectDir}/customSrc/secrets/Secrets.kt").absolutePath,
            secretsFile.absolutePath,
            "Secrets.kt file path should match customSrc/secrets/Secrets.kt"
        )
    }

    @Test
    fun `given invalid keys in properties when generateSecrets is called then skip invalid keys`() {
        val project: Project = ProjectBuilder.builder().build()
        File(project.projectDir, "local.properties").writeText(
            """
            VALID_KEY=ok_value
            invalid-key=should_skip
            _ANOTHER=allowed
        """.trimIndent()
        )
        val extension = SecretsPluginExtension(project)

        val generator = SecretsGenerator(project, extension)
        val secretsFile = generator.generateSecrets()

        val content = secretsFile.readText()
        assertTrue(content.contains("val VALID_KEY = \"ok_value\""), "VALID_KEY is valid")
        assertTrue(content.contains("val _ANOTHER = \"allowed\""), "_ANOTHER is valid as well")
        assertFalse(
            content.contains("invalid-key"),
            "invalid-key is not a valid identifier; should be skipped"
        )
    }
}
