import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish") version "0.31.0-rc2"
    alias(libs.plugins.kotlinMultiplatform) apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.tarasovvp"
version = "1.3.0"

dependencies {
    compileOnly(kotlin("gradle-plugin"))
    implementation(libs.kotlin.test)
    testImplementation(kotlin("gradle-plugin"))
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        create("kmpSecretsPlugin") {
            id = "io.github.tarasovvp.kmp-secrets-plugin"
            implementationClass = "com.vnteam.KMPSecretsPlugin"
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.tarasovvp",
        artifactId = "kmp-secrets-plugin",
        version = "1.3.0"
    )

    pom {
        name.set("KMP Secrets Plugin")
        description.set(
            "KMP Secrets Plugin is a Gradle plugin that converts key-value "
                    + "pairs from local.properties into a type-safe Kotlin object "
                    + "(Secrets) for Kotlin Multiplatform projects. The plugin works "
                    + "on Android, iOS, Desktop and Web targets, supports "
                    + "Configuration Cache, automatically adds the generated "
                    + "Secrets.kt file to .gitignore, and allows configuring the "
                    + "output directory. Default output path is "
                    + "src/commonMain/kotlin/secrets/Secrets.kt. To use, apply "
                    + "id(\"io.github.tarasovvp.kmp-secrets-plugin\") and add your "
                    + "secrets to local.properties in the corresponding module. "
                    + "Published under Apache-2.0."
        )
        url.set("https://github.com/TarasovVP/KMP-Secrets-Plugin")
        inceptionYear.set("2024")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("TarasovVP")
                name.set("Volodymyr Tarasov")
                email.set("vptarasov1998@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/TarasovVP/KMP-Secrets-Plugin.git")
            developerConnection.set("scm:git:ssh://github.com/TarasovVP/KMP-Secrets-Plugin.git")
            url.set("https://github.com/TarasovVP/KMP-Secrets-Plugin")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}