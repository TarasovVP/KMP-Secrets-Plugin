package com.vnteam

import org.gradle.api.Project

open class SecretsPluginExtension(project: Project) {
    var propertiesFile: String = project.rootProject.file(Constants.DEFAULT_LOCAL_PROPERTIES).absolutePath
    var outputDir: String = "${project.projectDir}/${Constants.DEFAULT_OUTPUT_DIR}"
    var triggerTask: String = Constants.DEFAULT_TRIGGER_TASK
}