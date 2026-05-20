plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.googleServices) apply false
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