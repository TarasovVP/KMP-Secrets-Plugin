package com.vnteam

object Constants {
    const val SECRETS_EXTENSION_NAME = "secretsConfig"
    const val GENERATE_SECRETS_TASK = "generateSecrets"

    const val SECRETS_PACKAGE = "package"
    const val SECRETS_OBJECT = "object"
    const val SECRETS_PACKAGE_NAME = "secrets"
    const val SECRETS_OBJECT_NAME = "Properties"
    const val SECRETS_FILE_NAME = "Secrets.kt"
    const val LOCAL_PROPERTIES_FILE = "local.properties"

    // Extension
    const val DEFAULT_LOCAL_PROPERTIES = "local.properties"
    const val DEFAULT_OUTPUT_DIR = "src/commonMain/kotlin"
    const val DEFAULT_TRIGGER_TASK = "preBuild"

    // Messages
    const val ERROR_NO_PROPERTIES_FOUND = "❌ No local.properties found for module: "
    const val ERROR_LOCAL_PROPERTIES_NOT_FOUND = "local.properties file not found at "
    const val SUCCESS_SECRETS_GENERATED = "✅ Secrets.kt successfully generated at "

    // .gitignore
    const val GITIGNORE_FILE = ".gitignore"
    const val GITIGNORE_AUTO_GENERATED = "# Auto-generated .gitignore"
    const val GITIGNORE_NOT_FOUND = "🛠️ .gitignore not found, creating a new one..."
    const val GITIGNORE_ADDING_ENTRY = "🛠️ Adding to .gitignore: "
    const val GITIGNORE_ALREADY_IGNORED = "✅ Already ignored in .gitignore"
}
