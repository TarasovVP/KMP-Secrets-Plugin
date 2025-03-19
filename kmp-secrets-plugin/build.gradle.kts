plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    signing
}

group = "com.vnteam.kmp-secrets"
version = "1.0.0"

dependencies {
    compileOnly(kotlin("gradle-plugin"))
}

gradlePlugin {
    plugins {
        create("kmpSecretsPlugin") {
            id = "com.vnteam.kmp-secrets"
            implementationClass = "com.vnteam.KMPSecretsPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

signing {
    sign(publishing.publications["pluginMaven"])
}