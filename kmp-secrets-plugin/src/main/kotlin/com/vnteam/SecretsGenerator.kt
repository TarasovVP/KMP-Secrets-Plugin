package com.vnteam

import java.io.File
import java.util.Properties

class SecretsGenerator {

    fun generateSecrets(propsFile: File, outputFile: File): File {
        val properties = loadProperties(propsFile)
        val configDir = outputFile.parentFile
        configDir.mkdirs()

        val packageName = configDir.relativeTo(outputFile.parentFile.parentFile)
            .path.replace("/", ".")
        val content = buildString {
            appendLine("${Constants.SECRETS_PACKAGE} $packageName")
            appendLine()
            appendLine("${Constants.SECRETS_OBJECT} ${Constants.SECRETS_OBJECT_NAME} {")

            properties.forEach { (keyAny, value) ->
                val key = keyAny.toString()
                if (isValidKey(key)) {
                    appendLine("    const val $key = \"$value\"")
                }
            }

            appendLine("}")
        }
        outputFile.writeText(content)
        return outputFile
    }

    private fun loadProperties(file: File): Properties {
        if (!file.exists()) {
            throw RuntimeException(Constants.ERROR_LOCAL_PROPERTIES_NOT_FOUND + file.absolutePath)
        }
        return Properties().apply {
            load(file.inputStream())
        }
    }

    private fun isValidKey(key: String): Boolean {
        return key.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))
    }
}
