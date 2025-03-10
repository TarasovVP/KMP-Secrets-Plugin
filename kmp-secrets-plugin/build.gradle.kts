plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.vnteam.kmp-secrets"
version = "1.0.0"

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