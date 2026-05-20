# FinvoraAI Code Quality & Formatting Guide

This guide covers the static code analysis, formatting, and style rules implemented in the **FinvoraAI** project using `.editorconfig`, **Spotless (KtLint)**, and **Detekt**.

---

## 1. Directory Structure

The files configuration for code quality are located at:
*   `.editorconfig` (Root directory)
*   `gradle/libs.versions.toml` (Dependency and plugin versions)
*   `build.gradle.kts` (Plugin applications and configurations)
*   `config/detekt/detekt.yml` (Detekt rule configurations)

---

## 2. `.editorconfig` Setup

The `.editorconfig` file defines the editor configuration rules (like indentation, line lengths, and trailing comma preferences) for IDEs (such as Android Studio or IntelliJ IDEA).

### Exact Configuration (`.editorconfig`)
```ini
root = true

[*.{kt,kts}]
indent_size = 4
insert_final_newline = true
max_line_length = 120

ktlint_function_naming_ignore_when_annotated_with = Composable
ktlint_standard_no-wildcard-imports = disabled

ij_kotlin_allow_trailing_comma = false
ij_kotlin_allow_trailing_comma_on_call_site = false
ktlint_standard_trailing-comma-on-call-site = never
ktlint_standard_trailing-comma-on-declaration-site = never
```

---

## 3. Dependency & Version Declarations

Before applying the plugins, they must be registered in the Gradle Version Catalog (`gradle/libs.versions.toml`).

### Exact Versions and Plugins (`gradle/libs.versions.toml`)

```toml
[versions]
spotless = "6.25.0"
detekt = "1.23.6"

# ... other versions

[plugins]
spotless = { id = "com.diffplug.spotless", version.ref = "spotless" }
detekt = { id = "io.gitlab.arturbosch.detekt", version.ref = "detekt" }
```

---

## 4. Gradle Build Configuration

The plugins are applied and customized in the root `build.gradle.kts` file.

### Exact Gradle Configuration (`build.gradle.kts`)

```kotlin
plugins {
    // ... other plugins
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/.gradle/**")

        ktlint().editorConfigOverride(
            mapOf(
                "ij_kotlin_allow_trailing_comma" to "false",
                "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
                "ktlint_standard_trailing-comma-on-call-site" to "never",
                "ktlint_standard_trailing-comma-on-declaration-site" to "never",
                "ktlint_standard_no-wildcard-imports" to "disabled"
            )
        )
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    source.setFrom(files(
        "composeApp/src/commonMain/kotlin",
        "composeApp/src/androidMain/kotlin",
        "composeApp/src/iosMain/kotlin"
    ))
}
```

---

## 5. Detekt Rules Configuration

Detekt is a static code analysis tool for Kotlin. Rules are configured in `config/detekt/detekt.yml`.

### Exact Rules Configuration (`config/detekt/detekt.yml`)

```yaml
style:
  MagicNumber:
    active: true
    ignoreNumbers: ['0', '1', '-1']
    ignorePropertyDeclaration: true

  ForbiddenImport:
    active: true
    imports:
      - 'android.graphics.Color'
      - 'androidx.compose.ui.graphics.Color'
    excludes:
      - '**/ui/**'

  MaxLineLength:
    active: true
    maxLineLength: 120

  WildcardImport:
    active: true
    excludeImports:
      - 'kotlinx.android.synthetic.*'
      - 'androidx.compose.foundation.layout.*'
      - 'androidx.compose.runtime.*'
      - 'androidx.compose.material3.*'
      - 'finvoraai.composeapp.generated.resources.*'
      - 'com.finvoraai.personalfinancemanager.finvora.ui.theme.*'
      - 'com.finvoraai.personalfinancemanager.finvora.ui.uiutils.*'
      - 'androidx.compose.animation.core.*'
      - 'platform.Foundation.*'
      - 'org.jetbrains.compose.resources.*'

  UnusedImports:
    active: true

  ReturnCount:
    active: false

naming:
  FunctionNaming:
    active: true
    functionPattern: '[a-zA-Z][a-zA-Z0-9]*'
  MatchingDeclarationName:
    active: false

complexity:
  LongMethod:
    active: true
    threshold: 300

  StringLiteralDuplication:
    active: true
    threshold: 3
    excludes:
      - '**/ui/**'
      - '**/*ShareHelper.kt'
      - '**/Platform*.kt'
      - '**/*Database*.kt'
      - '**/Di*.kt'
      - '**/*DataStore*.kt'

  TooManyFunctions:
    active: false

  LongParameterList:
    active: false

  CyclomaticComplexMethod:
    active: false

exceptions:
  TooGenericExceptionThrown:
    active: false
```

---

## 6. How to Use & Run the Tools

To ensure code complies with the project standards, use the following Gradle commands in your terminal:

### Spotless (Formatting)
Spotless automatically formats your Kotlin source code to comply with KtLint and `.editorconfig` rules (such as enforcing no trailing commas).

*   **Check code formatting (Dry Run):**
    ```bash
    # Windows PowerShell/CMD:
    .\gradlew spotlessCheck

    # Unix/macOS:
    ./gradlew spotlessCheck
    ```
*   **Apply formatting fixes automatically:**
    ```bash
    # Windows PowerShell/CMD:
    .\gradlew spotlessApply

    # Unix/macOS:
    ./gradlew spotlessApply
    ```

### Detekt (Static Analysis)
Detekt runs static analysis checks to catch code smells, magic numbers, forbidden imports, string duplications, and complexity violations.

*   **Run Detekt analysis:**
    ```bash
    # Windows PowerShell/CMD:
    .\gradlew detekt

    # Unix/macOS:
    ./gradlew detekt
    ```

---

## 7. Key Enforced Rules & Best Practices

1.  **Trailing Commas:** Absolutely forbidden. Spotless will automatically strip them, and `.editorconfig` configures IDEs to avoid adding them automatically.
2.  **Magic Numbers:** Blocked by Detekt (`MagicNumber` rule). Numbers other than `0`, `1`, and `-1` must be extracted to named constants/variables, except in property declarations.
3.  **Forbidden Imports:** To enforce clean architecture, importing framework-specific colors (e.g. `android.graphics.Color` or Compose's `Color` directly) is banned outside the `ui` package.
4.  **Max Line Length:** Strictly restricted to `120` characters.
5.  **Wildcard Imports:** Generally forbidden, but specific frameworks (like Compose, Kotlinx Serialization, and resources) are explicitly excluded to allow neat layout/runtime imports.
