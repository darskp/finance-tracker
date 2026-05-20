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

---

Happy coding! Let's keep the FinvoraAI codebase premium, beautiful, consistent, and clean! 🚀
