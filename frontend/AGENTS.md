# Repository Guidelines

## Project Structure & Module Organization
This repository is a single-module Android app built with Gradle Kotlin DSL and Jetpack Compose. The root contains shared build files: `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`, and the Gradle wrapper scripts. App code lives under `app/`.

Use these paths as the default map:
- `app/src/main/java/com/uet/sleeptracker/` - Kotlin source
- `app/src/main/java/com/uet/sleeptracker/ui/screens/` - feature screens
- `app/src/main/java/com/uet/sleeptracker/ui/components/` - reusable Compose UI
- `app/src/main/java/com/uet/sleeptracker/ui/theme/` - colors, typography, theme setup
- `app/src/main/res/` - XML resources, drawables, strings, launcher assets
- `app/src/test/` - local JVM tests
- `app/src/androidTest/` - instrumented/device tests

## Build, Test, and Development Commands
Run commands from the repository root.

- `.\gradlew.bat :app:assembleDebug` - build the debug APK
- `.\gradlew.bat :app:installDebug` - install the debug build on a connected device/emulator
- `.\gradlew.bat :app:testDebugUnitTest` - run JVM unit tests in `app/src/test`
- `.\gradlew.bat :app:connectedDebugAndroidTest` - run instrumentation and Compose UI tests
- `.\gradlew.bat :app:compileDebugKotlin` - fast Kotlin compile check for UI changes

## Coding Style & Naming Conventions
Follow Kotlin conventions and keep the code consistent with the existing Compose codebase: 4-space indentation, trailing commas where the IDE adds them, and concise composable functions. Name screens and components in `PascalCase` with `Screen`, `Layout`, or `Button` suffixes when appropriate, for example `ProfileScreen.kt` or `SleepButtons.kt`. Keep package names lowercase and grouped by feature under `ui/screens/...`.

## Testing Guidelines
Use JUnit 4 for local tests and AndroidX instrumentation/Compose test APIs for device tests. Name test files after the target class or screen, for example `HomeScreenTest.kt`, and write test methods as behavior statements. Add focused tests for navigation, screen state, and reusable UI logic when changing shared components.

## Commit & Pull Request Guidelines
Recent history follows short conventional-style subjects such as `feat: ...`, `refactor: ...`, and `chore: ...`. Keep that format and make each commit scoped to one change. Pull requests should include a brief summary, affected screens or flows, linked issues when applicable, and screenshots or recordings for UI work.

## Configuration Notes
Do not commit machine-specific changes from `local.properties`. Keep SDK paths local, and place new dependency versions in `gradle/libs.versions.toml` rather than hardcoding them in module build files.
