package com.vnteam


import org.gradle.api.Project

open class SecretsPluginExtension(project: Project) {
    var propertiesFile: String = project.rootProject.file("local.properties").absolutePath
    var outputDir: String = "${project.projectDir}/src/commonMain/kotlin"
    var triggerTask: String = "preBuild"
}