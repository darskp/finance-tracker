**NO HARD CODE STRINGS THEME COLORS CODES SPACING OR FORMATTING/UTILITY LOGIC IN UI COMPONENTS**

# 🎨 FinvoraAI Developer Cheat Sheet & Design System Guide

Welcome! This guide is designed as the ultimate cheat sheet for all developers (including brand new freshers and junior engineers) to understand, implement, and maintain the UI tokens used in **FinvoraAI**.

To maintain a pixel-perfect, premium financial SaaS product that your team and users will love, we strictly enforce a design-system-first approach. Follow this guide to build layouts that align 100% with the client's expectations and industry-leading standards.

---

## 📌 TABLE OF CONTENTS
1. [🚨 The 3 Commandments of FinvoraAI Development](#-the-3-commandments-of-finvoraai-development)
2. [🧭 Quick Selection Guide: "Which Text Style Do I Pick?"](#-quick-selection-guide-which-text-style-do-i-pick)
3. [🌌 Reusable Ambient Screen Backgrounds (`AppBackgroundScreen`)](#-reusable-ambient-screen-backgrounds-appbackgroundscreen)
4. [📱 Tablet & Large Screen Responsiveness](#-tablet--large-screen-responsiveness)
5. [📐 Spacing & Dimension Tokens Reference](#-spacing--dimension-tokens-reference)
6. [🅰️ Typography Hierarchy Breakdown](#-typography-hierarchy-breakdown)
7. [🖼️ How to Add & Use Images and Icons Correctly](#%EF%B8%8F-how-to-add--use-images-and-icons-correctly)
8. [📋 Copy-Paste Starter Templates (Quick Cheat-Codes)](#-copy-paste-starter-templates-quick-cheat-codes)

---

## 🚨 THE 5 COMMANDMENTS OF FINVORAAI DEVELOPMENT

Before writing any Composable code, memorize these five rules:

### 1. 🔤 NEVER Hardcode Strings
Always declare strings inside `strings.xml` and load them via the generated resource accessor:
* **Incorrect:** `Text(text = "Email Address")`
* **Correct:** `Text(text = stringResource(Res.string.auth_email_label))`

### 2. 📐 NEVER Hardcode Layout Spacings, Paddings, or Sizes
Always use pre-defined spacing tokens inside the `Spacing` object:
* **Incorrect:** `Spacer(modifier = Modifier.height(16.dp))` or `Modifier.padding(8.dp)`
* **Correct:** `Spacer(modifier = Modifier.height(Spacing.s4))` or `Modifier.padding(Spacing.s2)`

### 3. 🎨 NEVER Hardcode Color Hex Codes
Always use the dynamic theme palette injected from the local composition local so that the app supports dark/light transitions flawlessly:
* **Incorrect:** `color = Color(0xFF161D19)` or `color = Color.Green`
* **Correct:** `color = palette.surface` or `color = palette.primary`

### 4. 🛠️ NEVER Inline Utility or Formatting Logic in UI Components
Always place formatting, date manipulation, string masking, or mathematical abbreviation algorithms as extensions or functions inside the `uiutils` package (e.g., [UiUtils.kt](file:///C:/Users/darsw/Downloads/practice/FinvoraAI/composeApp/src/commonMain/kotlin/com/finvoraai/personalfinancemanager/finvora/ui/uiutils/UiUtils.kt)).
* **Incorrect:** Slicing indices and appending `******` to mask an email inside the `OtpDialog` composable.
* **Correct:** `email.maskEmail()` using the reusable `String.maskEmail()` extension inside `UiUtils.kt`.

### 📦 5. NEVER Use Fully Qualified Class Names Inline in UI Code
Keep imports exclusively at the top of the file. Avoid muddying function bodies with full package qualifiers such as `com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette.current`.
* **Incorrect:**
  ```kotlin
  val palette = com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette.current
  ```
* **Correct:**
  Place the import at the top of the file:
  ```kotlin
  import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
  ```
  Then use the clean reference directly:
  ```kotlin
  val palette = LocalAppPalette.current
  ```

---

## 🧭 QUICK SELECTION GUIDE: "WHICH TEXT STYLE DO I PICK?"

Use this cheat sheet to choose the correct styling in 2 seconds flat based on what you are building:

| What are you building? | The Style to Use | The Typography Function |
| :--- | :--- | :--- |
| **Main Screen Big Header** | Premium 32sp bold title | `H1TextStyle()` |
| **Secondary Page Title / Card Header** | Sleek 28sp bold subtitle | `H2TextStyle()` |
| **Standard Section Header** | 20sp bold label | `H4TextStyle()` |
| **Button Label / Main Clickable Link** | 16sp high-legibility bold | `H6TextStyle()` |
| **Standard Input Text (Typed content)** | 16sp highly legible regular text | `BodyLarge()` |
| **Form Label (Above Input Box)** | 14sp medium label | `BodyNormal().copy(fontWeight = FontWeight.Medium)` |
| **Subtitle / Sub-paragraph Description** | 14sp secondary detail text | `BodyNormal()` (with `palette.textSecondary`) |
| **Error Messages / Password Strength helper**| 12sp compact warning | `BodySmall()` (with `palette.error`) |
| **Small Badges / Timestamp / Smallest Metadata**| 10sp micro caption text | `BodyXSmall()` |

---

## 🌌 REUSABLE AMBIENT SCREEN BACKGROUNDS (`AppBackgroundScreen`)

Do **NOT** reinvent the wheel by setting up custom colors or background modifiers on new screens! FinvoraAI has a premium, global background screen wrapper [AppBackgroundScreen.kt](file:///C:/Users/darsw/Downloads/practice/FinvoraAI/composeApp/src/commonMain/kotlin/com/finvoraai/personalfinancemanager/finvora/ui/components/AppBackgroundScreen.kt) that:
1. Automatically handles Light and Dark mode canvas rendering.
2. Embeds beautiful glassmorphic glowing ambient halos to look state-of-the-art.
3. Centers and scales content beautifully.

### ⚙️ Parameters Breakdown
When creating a screen, wrap your root content inside `AppBackgroundScreen` and pass these configuration toggles:

* **Clean Standard Background (Recommended for Dashboard, Charts, Settings):**
  ```kotlin
  AppBackgroundScreen {
      // Your dashboard contents here...
  }
  ```
  *Renders a clean background canvas with soft ambient glow circles on the top-right and bottom-left to keep layout readability at 100%.*

* **Header Halo Glow (Recommended for Auth, Onboarding, Welcome screens):**
  ```kotlin
  AppBackgroundScreen(showTopCenterBlur = true) {
      // Your sign-in / welcome screen contents here...
  }
  ```
  *Adds a beautiful top-center radial halo behind your main branding logo, giving it a high-end visual edge.*

* **Floating Ambient Particles (Recommended for promotional/onboarding screens):**
  ```kotlin
  AppBackgroundScreen(shouldShowDotsAndIcons = true) {
      // Your promotional screen contents here...
  }
  ```
  *Scatters subtle, high-fidelity ambient floating particles across the canvas background.*

---

## 📱 TABLET & LARGE SCREEN RESPONSIVENESS

When running on a large Tablet, a Foldable device, or in landscape mode, UI components should **NEVER** stretch all the way across the wide screen. It looks bloated, unprofessional, and is hard to read.

### 🛡️ The Max Width Rule (`Spacing.authMaxWidth`)
To ensure your new feature is automatically 100% tablet-responsive, enforce the standard **`Spacing.authMaxWidth`** constraint (`480.dp`) on your root column:

```kotlin
Column(
    modifier = Modifier
        .widthIn(max = Spacing.authMaxWidth) // 👈 Automatically caps width on tablets!
        .fillMaxWidth() // 👈 Automatically fills full width on mobile phones!
        .padding(horizontal = Spacing.s6),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    // Your UI Components...
}
```

### 🧠 How It Works:
* **On a Phone (e.g. 360dp wide):** The column ignores the `widthIn(max = 480.dp)` rule because the phone screen is smaller than `480.dp`. It fills the full screen width elegantly.
* **On a Tablet (e.g. 800dp wide):** The column caps its maximum width at `480.dp` and centers itself. It looks perfectly clean, professional, and consistent with the Figma mockup!

---

## 📐 SPACING & DIMENSION TOKENS REFERENCE

The `Spacing` token system (defined in `ThemeType.kt`) guarantees consistent margins, paddings, and alignment across all screen sizes (mobile and tablet):

| Token Name | DP Size | Common Use Case |
| :--- | :--- | :--- |
| `Spacing.hairline` | `1.dp` | Sleek boundaries, divider lines, and card/logo borders. |
| `Spacing.sHalf` | `2.dp` | Micro layout adjustments, very tight label spacings. |
| `Spacing.s1` | `4.dp` | Tiny paddings, small gaps between sub-items. |
| `Spacing.s2` | `8.dp` | General component spacing, padding between text elements. |
| `Spacing.s3` | `12.dp` | Corner shapes for rounded input boxes, cards, and buttons. |
| `Spacing.s3Half` | `14.dp` | Premium compact horizontal content padding inside text fields. |
| `Spacing.s4` | `16.dp` | Standard screen edge padding, standard list row spacing. |
| `Spacing.s5` | `20.dp` | Intermediate separation, default icon size inside input boxes. |
| `Spacing.s6` | `24.dp` | Major separation between content sections. |
| `Spacing.s8` | `32.dp` | Prominent separators, bottom screen spacer cushions. |
| `Spacing.s9` | `36.dp` | Branding and logo inner image sizes. |
| `Spacing.s13` | `52.dp` | Standard input box vertical height. |
| `Spacing.s16` | `64.dp` | Outer dimension of logo boxes and profile avatars. |
| `Spacing.s20` | `80.dp` | Generous top header spacer cushion (used below app-bar). |
| `Spacing.authMaxWidth` | `480.dp` | Maximum layout width constraint (keeps UI responsive on Tablets!). |

---

## 🅰️ TYPOGRAPHY HIERARCHY BREAKDOWN

Our main font family is **Manrope**, a beautiful modern geometric sans-serif loaded dynamically. Use the custom Compose font functions in `TextStyle.kt` to enforce correct typography hierarchies:

### 1. Headings (Titles & Big Labels)
```kotlin
H0TextStyle() // 40.sp, bold - Ultra large titles
H1TextStyle() // 32.sp, bold - Main screen titles
H2TextStyle() // 28.sp, bold - Page subtitles
H3TextStyle() // 24.sp, semibold - Standard headers
H4TextStyle() // 20.sp, semibold - Small headers
H5TextStyle() // 18.sp, semibold - Row labels
H6TextStyle() // 16.sp, semibold - Button text, primary clickable elements
```

### 2. Body Text (Paragraphs & general metadata)
```kotlin
BodyXXLarge() // 20.sp, medium - Standout introductory paragraphs
BodyXLarge()  // 18.sp, medium - Highlighted descriptions
BodyLarge()   // 16.sp, medium - Standard typing/reading text inside fields
BodyNormal()  // 14.sp, medium - Subtitles, description paragraphs
BodySmall()   // 12.sp, medium - Helper guides, compact captions
BodyXSmall()  // 10.sp, regular - Tiny metadata, timestamps
```

---

## 🖼️ HOW TO ADD & USE IMAGES AND ICONS CORRECTLY

### A. How to Add New Assets
1. Put your vector files (`.xml` format) or image files (`.png`, `.webp`) in the drawable resources folder:
   `composeApp/src/commonMain/composeResources/drawable/`
2. Keep file names lower_case with underscores (e.g. `ic_logo_symbol.xml`, `img_welcome_banner.png`).

### B. How to Use them in Composables
Always reference them through the generated `Res.drawable` object inside a `painterResource` wrapper. **Never** hardcode local disk paths or raw byte loaders:

```kotlin
// Example 1: Loading an Icon inside a text field
AppTextField(
    value = email,
    onValueChange = { email = it },
    label = stringResource(Res.string.auth_email_label),
    leadingIconPainter = painterResource(Res.drawable.ic_email) // 👈 Safe Dynamic Reference!
)

// Example 2: Displaying a Decorative Banner
Image(
    painter = painterResource(Res.drawable.finvora_financial_visualization),
    contentDescription = stringResource(Res.string.cd_welcome_banner),
    modifier = Modifier.fillMaxWidth()
)
```

---

## 📋 COPY-PASTE STARTER TEMPLATES (QUICK CHEAT-CODES)

Freshers: Need to build a standard screen section fast? Just copy, paste, and change the string reference!

### 1. Future Screen Template (e.g. a new Dashboard)
```kotlin
package com.finvoraai.personalfinancemanager.finvora.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing

@Composable
fun DashboardScreen() {
    val palette = LocalAppPalette.current
    
    // 1. Wrap the entire UI canvas inside the reusable background wrapper!
    AppBackgroundScreen {
        Column(
            modifier = Modifier
                .widthIn(max = Spacing.authMaxWidth) // 👈 Enforce Tablet Responsiveness!
                .fillMaxSize()
                .padding(horizontal = Spacing.s4), // 👈 Tokenized Margin
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.s20)) // 👈 Tokenized Header Spacing

            // 2. Add your premium visual contents
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.displayMedium,
                color = palette.textPrimary
            )
        }
    }
}
```

### 2. The Standard Title & Subtitle Section
```kotlin
// 1. Fetch theme parameters
val palette = LocalAppPalette.current

Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    // 2. Main Title
    Text(
        text = stringResource(Res.string.auth_app_name), // 👈 Change this to your string!
        style = MaterialTheme.typography.displayMedium,
        color = palette.primary,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(Spacing.s3)) // 👈 12dp Spacer

    // 3. Description Subtitle
    Text(
        text = stringResource(Res.string.auth_sign_in_subtitle), // 👈 Change this to your string!
        style = MaterialTheme.typography.bodyLarge,
        color = palette.textSecondary,
        textAlign = TextAlign.Center
    )
}
```

### 3. Standard Form Text Input Box
```kotlin
AppTextField(
    value = email,
    onValueChange = { email = it },
    label = stringResource(Res.string.auth_email_label), // 👈 Change this to your label string!
    placeholder = stringResource(Res.string.auth_email_placeholder), // 👈 Change this to your placeholder!
    leadingIconPainter = painterResource(Res.drawable.ic_email) // 👈 Load icon safely
)
```

### 4. Clickable Inline Footer Link
```kotlin
Row(
    modifier = Modifier.padding(vertical = Spacing.s1),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = stringResource(Res.string.auth_dont_have_account_prefix), // "Don't have an account? "
        style = MaterialTheme.typography.bodyMedium,
        color = palette.textSecondary
    )
    Text(
        text = stringResource(Res.string.auth_sign_up_link), // "Sign Up" (Only this is clickable!)
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = palette.primary,
        modifier = Modifier.clickable { 
            // 👈 Paste your navigation or click action here!
        }
    )
}
```

### 5. Primary Brand Button
```kotlin
FinvoraButton(
    text = stringResource(Res.string.auth_sign_in_btn), // 👈 Change this to your button text string!
    onClick = { 
        // 👈 Paste button action here!
    },
    style = ButtonStyle.PRIMARY,
    enabled = isFormValid // 👈 Bind to form validation states
)
```

You can add this as a new commandment without deleting anything. This fits perfectly with your architecture rules.

---

## 🧠 6. NEVER Place Business Logic, API Calls, or State Management Logic Directly Inside UI Composables

Composable functions should remain purely focused on rendering UI and handling lightweight UI interactions only.
All business logic, validation, authentication flows, repository calls, navigation events, session handling, and state mutations must live inside the `ViewModel`, `UseCase`, or domain/data layers.

The UI layer should simply:

* Observe state
* Render state
* Dispatch user actions/events

### ✅ Correct Responsibilities of a Composable

* Rendering UI components
* Animations
* Keyboard/focus handling
* Scroll states
* Collecting ViewModel state
* Triggering callbacks/events

### ❌ What Should NEVER Live Inside UI

* API calls
* Authentication logic
* Database operations
* Validation algorithms
* Data formatting logic
* Session/token handling
* Business calculations
* Navigation decision logic
* Mutable app state ownership

---

### ❌ Incorrect Example

```kotlin
LaunchedEffect(uiState) {
    if (uiState is AuthUiState.Success) {
        saveUserToken()
        navigateToDashboard()
    }
}
```

```kotlin
if (email.contains("@")) {
    authRepository.login(email, password)
}
```

---

### ✅ Correct Example

#### ViewModel

```kotlin
fun signIn(email: String, password: String) {
    viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        authRepository.signIn(email, password)

        _events.emit(AuthEvent.NavigateDashboard)
    }
}
```

#### UI

```kotlin
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when(event) {
            is AuthEvent.NavigateDashboard -> {
                onSignInSuccess()
            }
        }
    }
}
```

---

### 🏗️ FinvoraAI Architecture Philosophy

```text id="yqsl2k"
UI Layer        → Draws pixels only
ViewModel       → Manages screen state
Domain Layer    → Handles business rules
Repository      → Handles data/API sources
```

---

### 💡 Golden Rule

If the logic can exist without Compose UI rendering, it most likely does NOT belong inside the Composable.

Yes — your guide is already very strong, but a few additional “industry-grade” rules would make it feel like a real enterprise engineering handbook used inside top startups/companies.

These are the most valuable additions you can add next:

---

# 🔄 7. SINGLE SOURCE OF TRUTH (SSOT)

Every screen should receive its state from one centralized source only — usually the `ViewModel`.

Never duplicate state across:

* UI
* Repository
* Navigation
* Local mutable states

### ❌ Incorrect

```kotlin id="58t8q7"
var loading by remember { mutableStateOf(false) }
```

while ALSO having:

```kotlin id="hbdw7z"
uiState.isLoading
```

---

### ✅ Correct

```kotlin id="ltmjlwm"
val uiState by viewModel.uiState.collectAsState()
```

UI should render directly from state.

---

# ♻️ 8. STATEFLOW FOR STATE, SHAREDFLOW FOR EVENTS

Use the correct reactive stream type.

| Purpose             | Use          |
| ------------------- | ------------ |
| Persistent UI State | `StateFlow`  |
| One-time Events     | `SharedFlow` |
| Navigation          | `SharedFlow` |
| Snackbar/Toast      | `SharedFlow` |

---

### ✅ Example

```kotlin id="6q8jdu"
StateFlow<AuthUiState>
SharedFlow<AuthEvent>
```

This is modern Android architecture standard.

---

# 🧪 9. ALL BUSINESS LOGIC MUST BE TESTABLE

If logic cannot be unit-tested without Compose rendering, the architecture is wrong.

### ❌ Incorrect

```kotlin id="x24n1x"
if (email.length > 5) {
    // inside composable
}
```

### ✅ Correct

```kotlin id="iqz2j0"
AuthValidator.validateEmail(email)
```

Composable should not contain decision-heavy logic.

---

# 🧱 10. FEATURE-FIRST PACKAGE STRUCTURE

Always organize code by feature, not by file type.

### ❌ Incorrect

```text id="lt9f2q"
screens/
viewmodels/
repositories/
```

### ✅ Correct

```text id="c2vbh5"
feature/
 ├── auth/
 ├── dashboard/
 ├── onboarding/
```

This scales much better in large teams.

---

# 🧹 11. UI COMPONENTS MUST REMAIN REUSABLE

Never tightly couple reusable components to:

* screen-specific logic
* navigation
* repositories
* ViewModels

### ❌ Incorrect

```kotlin id="g6x8hh"
AppButton(
    onClick = {
        authViewModel.signIn()
    }
)
```

### ✅ Correct

```kotlin id="6w7n2q"
AppButton(
    onClick = onSignInClick
)
```

Pass callbacks from parent screens.

---

# 🚫 12. NEVER TRIGGER NAVIGATION DIRECTLY FROM RECOMPOSITION

Navigation should happen only from:

* events
* side effects
* action handlers

Never from plain composable rendering.

### ❌ Incorrect

```kotlin id="7rl5x8"
if (uiState.isLoggedIn) {
    navController.navigate("home")
}
```

---

### ✅ Correct

```kotlin id="9x6cuj"
LaunchedEffect(Unit) {
    viewModel.events.collect { ... }
}
```

This avoids duplicate navigation bugs.

---

# ⚡ 13. KEEP COMPOSABLES SMALL & SPLIT UI SECTIONS

If a composable becomes too large:

* split sections
* extract reusable blocks
* separate form sections

### Recommended Rule

```text id="7h0j37"
~200 lines max per composable
```

Large composables become impossible to maintain.

---

# 🧠 14. UI SHOULD BE STATELESS WHEN POSSIBLE

Prefer:

```kotlin id="71rk17"
ProfileContent(
    state = uiState,
    onAction = {}
)
```

instead of internal hidden mutable states.

This improves:

* previews
* testing
* reusability

---

# 🔐 15. NEVER STORE SENSITIVE TOKENS MANUALLY

Since you use Clerk:

### ❌ Never

* save JWT manually
* save auth token in preferences
* manage refresh tokens manually

### ✅ Correct

Let Clerk manage:

* sessions
* persistence
* refresh lifecycle

Industry auth providers already solve this securely.

---

# 📡 16. REPOSITORIES SHOULD HIDE DATA SOURCES

ViewModel should never know:

* Retrofit
* Firebase
* SQLDelight
* Clerk SDK internals

### ❌ Incorrect

```kotlin id="8l9hzr"
FirebaseAuth.signInWithEmailAndPassword()
```

inside ViewModel.

### ✅ Correct

```kotlin id="p1skl8"
authRepository.signIn()
```

This keeps architecture clean.

---

# 🎯 17. ALWAYS DESIGN FOR DARK MODE FIRST

Fintech apps are heavily dark-mode focused.

Every component should support:

* dynamic palette
* contrast accessibility
* AMOLED readability

Never assume white backgrounds.

---

# 📦 18. NO MAGIC NUMBERS OR RANDOM VALUES

Avoid:

```kotlin id="i8ykot"
alpha = 0.73f
padding = 19.dp
```

Create semantic tokens/constants instead.

---

# 🚀 19. OPTIMIZE RECOMPOSITION

Avoid unnecessary recompositions:

* use immutable UI state
* remember expensive calculations
* avoid passing unstable objects

This becomes critical in:

* dashboards
* charts
* finance lists

---

# 🐞 20. DEVELOPMENT DEBUG OVERLAY SYSTEM (MANDATORY FOR DEV BUILDS)

FinvoraAI includes an internal Developer Debug Overlay System inspired by Redux DevTools / React state inspectors.

This system exists to help developers visually inspect:

ViewModel state
API responses
Room database rows
Navigation state
Auth/session state
Loading/error states
Computed frontend variables
Recomposition/debug traces

directly inside the running app — even on APK-only builds without Android Studio attached.

🎯 PURPOSE

Modern finance apps contain:

complex auth flows
onboarding routing
session restoration
multiple API layers
calculated frontend state
database synchronization

Relying only on Logcat becomes painful.

The Debug Overlay provides:

**React Redux DevTools-like visibility** BUT inside Jetpack Compose!

### 🎯 MANDATORY CHECKLIST BEFORE PUSHING NEW FEATURES
Every time you build a new feature (e.g. `IncomeViewModel`), you **must** expose its state to the Debug Overlay before submitting a PR.

👉 **REFER TO THIS GUIDE FOR INSTRUCTIONS:** 
[docs/DEBUG_OVERLAY_GUIDE.md](file:///c:/Users/darsw/Downloads/practice/FinvoraAI/docs/DEBUG_OVERLAY_GUIDE.md)

**Quick Usage:**
```kotlin
// Inside your new ViewModel:
DebugLogger.generic("Income", "balanceState", state)
```

# 🏛️ 21. BUILD FOR SCALE, NOT JUST FOR TODAY

Every new feature should assume:

* multiple developers
* future redesigns
* backend changes
* localization
* tablet support
* offline mode

Good architecture survives growth.

.\gradlew clean; adb uninstall com.finvoraai.personalfinancemanager; .\gradlew :composeApp:installDebug; adb shell monkey -p com.finvoraai.personalfinancemanager -c android.intent.category.LAUNCHER 1

.\gradlew :composeApp:installDebug

 adb logcat -s System.out:I | Select-String "CLERK"

.\gradlew :composeApp:assembleDebug                       

.\gradlew clean                                           


1) Auto-format everything (Spotless)
./gradlew spotlessApply

This:
Removes trailing commas
Fixes ktlint style
Fixes formatting everywhere


Run static analysis (Detekt)
./gradlew detekt

Must be BUILD SUCCESSFUL.
No warnings. No smells.



---

FINAL DEVELOPMENT CHECKLIST (MANDATORY BEFORE MARKING TASK COMPLETE)
Before closing any FinvoraAI task, PR, feature branch, or AI-generated implementation, ALL checklist items below MUST pass successfully.

If even one item fails, the task is considered incomplete.

🎯 Architecture & UI Validation Checklist
✅ UI & Design System
 No hardcoded strings

 No hardcoded colors

 No hardcoded spacing/padding/sizes

 No magic numbers

 Uses Spacing tokens correctly

 Uses theme palette correctly

 Supports dark mode properly

 Tablet responsiveness verified

 Components remain reusable

 No fully qualified imports inline

 No formatting/util logic inside UI

✅ Compose & Architecture
 No business logic inside composables

 UI only renders state

 State handled via StateFlow

 One-time events handled via SharedFlow

 Navigation triggered only from events/effects

 ViewModel is the single source of truth

 Repository abstracts data sources

 Composables kept modular and small

 Recomposition-safe implementation

 Stateless UI where possible

✅ Authentication & Security
 No manual token storage

 No JWT persistence in SharedPreferences

 Clerk session management used correctly

 Logout clears session properly

 Session restore tested successfully

✅ Code Quality
 No unused imports

 No dead code

 No duplicated logic

 Functions properly named

 Files organized feature-first

 Business logic testable independently

🧪 REQUIRED TERMINAL COMMANDS BEFORE TASK COMPLETION
1️⃣ Clean Project
./gradlew clean
2️⃣ Auto Format Entire Project (MANDATORY)
./gradlew spotlessApply
This ensures:

ktlint formatting passes

trailing commas fixed

spacing consistency

import cleanup

code style normalization

3️⃣ Run Static Analysis (MANDATORY)
./gradlew detekt
Requirements:

Must be BUILD SUCCESSFUL

No warnings

No smells

No architecture violations

4️⃣ Install Debug Build
./gradlew :composeApp:installDebug

5️⃣ Build APK
./gradlew :composeApp:assembleDebug

=🤖 AI MODEL / AGENT TASK COMPLETION RULE
Any AI-generated code contribution (ChatGPT, Claude, Gemini, Cursor, Copilot, Antigravity, etc.) MUST end with a completion summary using this exact structure:

## ✅ Task Completion Status

- [x] UI implemented
- [x] Dark mode supported
- [x] Tablet responsiveness verified
- [x] No hardcoded strings
- [x] No hardcoded colors
- [x] No hardcoded spacing
- [x] No business logic inside UI
- [x] StateFlow/SharedFlow used correctly
- [x] ViewModel integrated
- [x] Navigation handled safely
- [x] Spotless formatting applied
- [x] Detekt checks passed
- [x] Build successful
If any item is incomplete, it MUST remain unchecked.

🚨 FINAL NON-NEGOTIABLE RULE
A task is NOT considered complete until ALL of the following pass successfully:

✔ spotlessApply
✔ detekt
✔ assembleDebug
✔ installDebug
✔ runtime testing
✔ architecture validation
✔ design system validation

Happy coding! Let's keep the FinvoraAI codebase premium, scalable, maintainable, and industry-grade 🚀
