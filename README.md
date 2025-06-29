# KMP Secrets Plugin

Gradle plugin that turns key-value pairs from local.properties into a type-safe Kotlin object for Kotlin Multiplatform projects.

Group/ID: io.github.tarasovvp : kmp-secrets-plugin  
Latest:  ![Maven Central](https://img.shields.io/maven-central/v/io.github.tarasovvp/kmp-secrets-plugin)   
License: Apache-2.0

---

## Installation

### 1. Configure repositories
Add to your `settings.gradle.kts` file:

<pre>
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
</pre>

### 2. Apply the plugin
Add to your module's `build.gradle.kts` (only where needed):

<pre>
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("io.github.tarasovvp.kmp-secrets-plugin") version "1.2.0"
}
</pre>

---

## Quick Start

1. **Put secrets in `local.properties` of the same module**  
   *(the root file is used only if the module file is absent)*
<pre>
API_KEY=your_key
DEBUG=true
</pre>

2. **Build the project** with any compile/assemble task, e.g.:
<pre>
./gradlew assemble
</pre>

3. The plugin generates the file  
   `<moduleDir>/<outputDir>/secrets/Secrets.kt`

   * By default, `outputDir` is `src/commonMain/kotlin`, so the file is created at  
     `<moduleDir>/src/commonMain/kotlin/secrets/Secrets.kt`.
   * If you override `outputDir` in the `secrets { … }` block, the file will be created in that custom location, e.g.:
<pre>
secrets {
    outputDir = "$projectDir/generatedSecrets"
}
</pre>

4. **Git ignore is handled automatically**

* If a `.gitignore` exists, the relative path to `Secrets.kt` is appended.  
* If none exists, the plugin creates one with the path included.  

Your secrets will never be committed by mistake.

---

## Notes

Preferred format:
<pre>
API_KEY=sample_secret_data
</pre>

With special characters:
<pre>
QUOTED=He said "Hello"
WINDOWS_PATH=C:\Tools\bin
</pre>

Becomes: 
<pre>
object Secrets { 
    const val QUOTED = "He said \"Hello\"" 
    const val WINDOWS_PATH = "C:\\Tools\\bin" 
}
</pre>

---

## Compatibility

- Gradle 8.2+
- Kotlin 1.9/2.x
- Android/iOS/Desktop/Web targets
