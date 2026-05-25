# Professional Design System & Implementation Guide

This guide is the single source of truth for the Template Design System. It is designed for developers of all levels—from **beginners** to **managers**—to ensure the UI remains consistent, premium, and follows industry standards across all brands and themes.

---

## 1. Dynamic Theme Architecture
Our app supports multiple palettes: **Light, Dark, Forest, and Ocean**. To ensure your UI works perfectly in all themes:

- **Rule #1**: NEVER use hardcoded colors like `finvoraaiColors.Primary.color` for text or backgrounds.
- **Rule #2**: ALWAYS use `MaterialTheme.colorScheme` or `LocalAppPalette.current`.
- **Rule #3**: Text styles automatically use `onSurface` from the theme. If you need a different color, use `copy(color = ...)` with a theme-aware color.

---

## 2. Typography Reference (Manrope Family)
We use the **Manrope** font family with industry-standard line heights for maximum readability.

### Headings (Headline/Title)
Use these for page titles, card headers, and section breaks.

| Style | Font Size | Weight | Line Height | Use Case |
| :--- | :--- | :--- | :--- | :--- |
| `H1TextStyle()` | 32sp | Bold | 40sp | Hero headers, onboarding titles |
| `H2TextStyle()` | 28sp | Bold | 36sp | Page headers, main section titles |
| `H3TextStyle()` | 24sp | SemiBold| 32sp | Modal titles, secondary sections|
| `H4TextStyle()` | 20sp | SemiBold| 28sp | Card headers, small page titles |
| `H5TextStyle()` | 18sp | SemiBold| 24sp | Small feature titles |
| `H6TextStyle()` | 16sp | SemiBold| 24sp | Micro-headers, labels |

### Body & Paragraphs
Use these for descriptive text, list items, and metadata.

| Style | Font Size | Weight | Line Height | Use Case |
| :--- | :--- | :--- | :--- | :--- |
| `BodyXXLarge()` | 20sp | Medium | 28sp | Intro text, pull quotes |
| `BodyXLarge()` | 18sp | Medium | 26sp | Large body text |
| `BodyLarge()` | 16sp | Medium | 24sp | Standard paragraph text |
| `BodyNormal()` | 14sp | Medium | 20sp | Secondary text, descriptions |
| `BodySmall()` | 12sp | Medium | 16sp | Captions, button text |
| `BodyXSmall()` | 10sp | Regular | 14sp | Help text, legal, metadata |

---

## 3. Spacing & Grid System (8px Grid)
We follow a strict **8-point grid**. All vertical and horizontal gaps must use the `Spacing` tokens.

| Token | Pixels | Use Case |
| :--- | :--- | :--- |
| `Spacing.s1` | 4dp | Tight grouping (e.g., icon + text) |
| `Spacing.s2` | 8dp | Standard internal padding |
| `Spacing.s3` | 12dp | Moderate grouping |
| `Spacing.s4` | 16dp | standard page margins, gap between cards |
| `Spacing.s6` | 24dp | Vertical gap between major sections |
| `Spacing.s8` | 32dp | Large layout breaks |

### Usage Implementation:
```kotlin
Column(modifier = Modifier.padding(Spacing.s4)) {
    Text("My Tasks", style = H2TextStyle())
    VSpacer(Spacing.s2) // 8dp gap
    Text("You have 5 tasks today", style = BodyNormal())
}
```

---

## 4. UI Patterns & Snippets

### Pattern A: Standard Page Header
Use this at the top of every new screen.
```kotlin
Column(modifier = Modifier.padding(Spacing.s4)) {
    Text(
        text = "Account Settings",
        style = H2TextStyle()
    )
    VSpacer(Spacing.s1)
    Text(
        text = "Manage your security and profile preferences",
        style = BodyNormal().copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    )
}
```

### Pattern B: The Colored Section Label
Use this to categorize groups of items.
```kotlin
Text(
    text = "DONE",
    style = H6TextStyle().copy(
        color = MaterialTheme.colorScheme.primary, // Dynamic brand color
        fontWeight = FontWeight.Bold
    )
)
```

### Pattern C: Readable Paragraph
Best for "About Us" or "Help" sections where readability is key.
```kotlin
Text(
    text = "Our mission is to help you stay organized and productive.",
    style = BodyLarge()
)
```

---

## 5. Theme-Aware Icons
All icons should automatically adapt to the current theme. Use `LocalAppPalette.current` or `MaterialTheme.colorScheme` for tinting.

### Icon Implementation Example
```kotlin
import org.jetbrains.compose.resources.painterResource
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.home

Icon(
    painter = painterResource(Res.drawable.home),
    contentDescription = "Home",
    // Use onSurface for standard icons
    tint = MaterialTheme.colorScheme.onSurface 
)
```

---

## 6. Category & Custom Colors
For specific logic-based colors (like task categories), use `finvoraaiColors` enum. However, always check if a theme-aware version exists first.

### Category Color Best Practice
```kotlin
// Define a category color
val categoryColor = finvoraaiColors.SkyBlue.color

// Implementation
Box(
    modifier = Modifier
        .background(categoryColor.copy(alpha = 0.1f)) // Soft background
        .border(1.dp, categoryColor) // Solid border
) {
    Text("Work", color = categoryColor)
}
```

---

## 7. Checklist for PR Submission

### Developers (Self-Check)
- [ ] **Theme Awareness**: Does it work in both Dark and Forest themes?
- [ ] **Hardcoded Colors**: Did I remove all `0xFF...` or `finvoraaiColors.*` usage?
- [ ] **Spacing**: Are all gaps handled via `VSpacer`/`HSpacer` using `Spacing` tokens?
- [ ] **Typography**: Did I use the correct `H` or `Body` style for the hierarchy?

### Managers (Review Gate)
- [ ] **Visual Consistency**: Does it follow the 8px grid (multiples of 4/8)?
- [ ] **Architecture**: Does it use the provided Design System instead of ad-hoc styles?
- [ ] **Readability**: Are line heights applied via the custom text styles?

---
*Created for the Standardized Template PR. Version 1.0.*
