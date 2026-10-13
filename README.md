# KMP Secrets Plugin

[![Maven Central](https://img.shields.io/maven-central/v/io.github.tarasovvp/kmp-secrets-plugin)](https://central.sonatype.com/artifact/io.github.tarasovvp/kmp-secrets-plugin)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A Gradle plugin that generates a type-safe Kotlin object from `local.properties` for Kotlin Multiplatform projects.
It works with Android, iOS, Desktop, and Web targets while keeping generated values out of version control.

## Features

- Generates `secrets.Secrets` before Kotlin compilation.
- Uses a module-level `local.properties`, with a fallback to the root project file.
- Generates string constants and escapes quotes, slashes, and control characters.
- Adds the generated `Secrets.kt` path to `.gitignore` automatically.
- Supports a custom output directory.
- Is available from Maven Central.

## Installation

Add Maven Central to plugin resolution in `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
```

Apply the plugin to the KMP module that needs the generated values:

```kotlin
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("io.github.tarasovvp.kmp-secrets-plugin") version "1.3.0"
}
```

## Quick start

Add the values to `local.properties` in the same module:

```properties
API_KEY=your_key
DEBUG=true
```

If the module does not contain `local.properties`, the plugin uses the file from the project root.
Build the project with any compile or assemble task:

```shell
./gradlew assemble
```

The plugin generates `src/commonMain/kotlin/secrets/Secrets.kt`:

```kotlin
package secrets

object Secrets {
    const val API_KEY = "your_key"
    const val DEBUG = "true"
}
```

The generated values can then be accessed from common code:

```kotlin
import secrets.Secrets

val apiKey = Secrets.API_KEY
```

## Configuration

The default output directory is `src/commonMain/kotlin`. To change it, configure `secretsConfig` in the module's `build.gradle.kts`:

```kotlin
secretsConfig {
    outputDir = "$projectDir/generatedSecrets"
}
```

The `secrets` package and `Secrets.kt` file are created inside that directory.

## Escaping

Special characters in property values are escaped when Kotlin code is generated:

```properties
QUOTED=He said "Hello"
WINDOWS_PATH=C:\Tools\bin
```

```kotlin
object Secrets {
    const val QUOTED = "He said \"Hello\""
    const val WINDOWS_PATH = "C:\\Tools\\bin"
}
```

## Security note

The plugin reduces the risk of committing local values by adding the generated file to `.gitignore`.
It does not encrypt secrets or prevent them from being extracted from a compiled application. Do not treat client-side values as a secure storage mechanism for server credentials.

## Compatibility

- Gradle 8.2+
- Kotlin 1.9 and 2.x
- Android, iOS, Desktop, and Web targets

## Further reading

Read [Automating Work with Secrets in Kotlin Multiplatform](https://medium.com/@vptarasov/automating-work-with-secrets-in-kotlin-multiplatform-a2b4c587180b) for the motivation, implementation details, and publishing process behind the plugin.

## License

Licensed under the [Apache License 2.0](LICENSE).
