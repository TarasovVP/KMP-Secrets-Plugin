package com.vnteam

import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import  kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SecretsGeneratorTest {

    private fun runGenerator(
        propsFile: File,
        outputDir: File = File(propsFile.parentFile, "build/generated/secrets")
    ): File {
        outputDir.mkdirs()
        val secretsFile = File(outputDir, Constants.SECRETS_FILE_NAME)
        SecretsGenerator().generateSecrets(propsFile, secretsFile)
        return secretsFile
    }

    @Test
    fun `given localproperties in module dir when generateSecrets is called then use local properties file`() {
        val project = ProjectBuilder.builder().build()
        val propsFile = File(project.projectDir, "local.properties").apply {
            writeText("API_KEY=local_key\nINVALID-KEY=xxx")
        }

        val secretsFile = runGenerator(propsFile)

        assertTrue(secretsFile.exists())

        val content = secretsFile.readText()
        assertTrue(content.contains("const val API_KEY = \"local_key\""))
        assertFalse(content.contains("INVALID-KEY"))
    }

    @Test
    fun `given no localproperties in module but present in root when generateSecrets is called then use root properties file`() {
        val project = ProjectBuilder.builder().build()
        val propsFile = File(project.rootProject.projectDir, "local.properties").apply {
            writeText("ROOT_SECRET=root_secret_value")
        }

        val secretsFile = runGenerator(propsFile)

        assertTrue(secretsFile.exists())
        assertTrue(secretsFile.readText().contains("const val ROOT_SECRET = \"root_secret_value\""))
    }

    @Test
    fun `given no localproperties at all when generateSecrets is called then throw exception`() {
        val tmpDir = createTempDir()
        val propsFile = File(tmpDir, "missing.properties")

        val ex = assertFailsWith<RuntimeException> {
            SecretsGenerator().generateSecrets(propsFile, File(tmpDir, "Secrets.kt"))
        }
        assertTrue(ex.message!!.contains(Constants.ERROR_LOCAL_PROPERTIES_NOT_FOUND))
    }

    @Test
    fun `given valid propertiesfile when generateSecrets is called then create secrets file with correct package`() {
        val project = ProjectBuilder.builder().build()
        val propsFile = File(project.projectDir, "local.properties").apply {
            writeText("API_KEY=12345")
        }
        val outDir = File(project.projectDir, "customSrc/secrets")

        val secretsFile = runGenerator(propsFile, outDir)

        val content = secretsFile.readText()
        assertTrue(content.contains("package secrets"))
        assertTrue(content.contains("const val API_KEY = \"12345\""))
        assertEquals(
            File(outDir, Constants.SECRETS_FILE_NAME).absolutePath,
            secretsFile.absolutePath
        )
    }

    @Test
    fun `given invalid keys in properties when generateSecrets is called then skip invalid keys`() {
        val project = ProjectBuilder.builder().build()
        val propsFile = File(project.projectDir, "local.properties").apply {
            writeText(
                """
                VALID_KEY=ok_value
                invalid-key=should_skip
                _ANOTHER=allowed
                """.trimIndent()
            )
        }

        val secretsFile = runGenerator(propsFile)
        val content = secretsFile.readText()

        assertTrue(content.contains("const val VALID_KEY = \"ok_value\""))
        assertTrue(content.contains("const val _ANOTHER = \"allowed\""))
        assertFalse(content.contains("invalid-key"))
    }
}
