# Testing Guide

This project follows the **Industry Standard Triple-Threat Testing Architecture** for Kotlin Multiplatform (KMP). 

This guide is designed for developers of all levels to understand, run, and maintain our high-quality codebase.

---

## 🏗️ 1. Our Testing Architecture
We divide our tests into three specific layers to balance speed and reliability:

1.  **Logic Unit Tests** (`commonTest`): Verifies the "Brain" (ViewModels/Logic). Runs instantly on any machine.
2.  **UI Interaction Tests** (`androidUnitTestDebug`): Verifies that buttons and elements exist and work.
3.  **Visual Snapshot Tests** (`androidUnitTestDebug`): Captures pixel-perfect images of screens to prevent unintended UI changes.

---

## 🚀 2. Essential Commands (The Developer Toolbox)

Run these commands from the project root directory:

### ✅ Running All Tests
Runs every single logic and UI test in the project.
```powershell
.\gradlew.bat test
```

### 📊 Generating Coverage Reports
Generates a beautiful browser-based report showing exactly which lines of code are tested.
```powershell
.\gradlew.bat clean koverHtmlReport
```
*   **Report Path**: `composeApp/build/reports/kover/html/index.html`

### 📸 Managing Visual Snapshots (UI Photos)
We use **Roborazzi** for screenshot testing.

*   **Record New/Updated UI**: (Use this if you intentionally changed the design)
    ```powershell
    .\gradlew.bat recordRoborazziDebug
    ```
*   **Verify UI Consistency**: (Checks if the current UI matches our saved photos)
    ```powershell
    .\gradlew.bat verifyRoborazziDebug
    ```

---

## � 3. Professional Workflow (New Feature Cycle)

When you build a new feature (e.g., a "User Profile" page), follow this step-by-step cycle:

### Step 1: Analyze the "Brain" (Logic)
Create a `ViewModelTest.kt` in `src/commonTest`. Ensure the data logic is 100% correct.
```powershell
.\gradlew.bat :composeApp:testDebugUnitTest --tests "*YourNewViewModelTest"
```

### Step 2: Verify the UI Elements (Interaction)
Create a `ScreenTest.kt` in `src/androidUnitTestDebug` using Robolectric. Check if buttons/text appear.
```powershell
.\gradlew.bat :composeApp:testDebugUnitTest --tests "*YourNewScreenTest"
```

### Step 3: Global Safety Check (Regression)
Ensure your new feature didn't break anything else in the app.
```powershell
.\gradlew.bat test
```

### Step 4: Visual Approval (Snapshots)
Save the "Perfect" version of your new screen.
```powershell
.\gradlew.bat recordRoborazziDebug
```

### Step 5: Final Quality Audit (Coverage)
Check for any "if/else" branches you missed.
```powershell
.\gradlew.bat clean koverHtmlReport
```

---

## 4. Current Project Status

| Feature | Coverage | Tool |
| :--- | :--- | :--- |
| **ViewModels (Logic)** | 85%+ | Kotlin Test |
| **Data Filtering** | 90%+ | Kotlin Test |
| **All Main Screens (UI)** | 100% | Roborazzi |
| **Overall Failures** | 0 | ✅ All Passing |

---

## 🗺️ 5. Future Roadmap (What's Next?)
For a 100% professional coverage, the following is recommended:
1.  **Network Mocks**: Mocking API responses for the Finvora AI backend (Ktor/Clerk level).
2.  **UI Flow Testing**: Testing navigation from Splash -> Onboarding -> Home with integration tests.
3.  **Financial Screen Snapshots**: Capturing snapshots for dashboard, analytics, and AI insight screen variants.
4.  **Analytics Event Tests**: Unit tests for `AnalyticsEvent` and `AnalyticsManager` to verify correct event dispatch.

---

## 💡 6. Best Practices for Beginners
*   **Mirror Packages**: If your code is in `ui.viewModel`, put your test in the same folder structure under `commonTest`.
*   **Test Coroutines**: Always use `runTest` for asynchronous operations.
*   **Keep builds clean**: If reports aren't updating, run `.\gradlew.bat clean` first.

---


.\gradlew.bat test
When: Use this every hour while coding.
Goal: Make sure you haven't broken any calculation or logic.

.\gradlew.bat clean koverHtmlReport
When: Use this before you finish for the day.
Goal: Make sure all your new code is "Green" in the browser report.

.\gradlew.bat recordRoborazziDebug
When: Use this only when you intentionally change the design (like changing a color or moving a logo).
Goal: Save the new "Master Photo" of your screen.

.\gradlew.bat verifyRoborazziDebug
When: Use this before you release or send your code to a teammate.
Goal: Prove that the UI didn't change by accident.

.\gradlew.bat assembleDebug
