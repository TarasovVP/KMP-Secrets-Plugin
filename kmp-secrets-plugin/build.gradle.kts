import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish") version "0.31.0-rc2"
    alias(libs.plugins.kotlinMultiplatform) apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.tarasovvp"
version = "1.0.0"

dependencies {
    compileOnly(kotlin("gradle-plugin"))
    implementation(libs.kotlin.test)
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
        version = "1.0.0"
    )

    pom {
        name.set("KMP Secrets Plugin")
        description.set("Gradle plugin for generating secrets in Kotlin Multiplatform projects.")
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