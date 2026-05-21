# iOS Auth Integration Guide

## Architecture

```
commonMain (SHARED — zero iOS changes needed)
  feature/auth/
    ├── AuthViewModel.kt        # Shared business logic + state
    ├── AuthUiState.kt          # Shared sealed UI states
    ├── AuthEvent.kt            # Shared one-shot navigation events
    ├── SignInScreen.kt         # Shared Compose UI
    ├── SignUpScreen.kt         # Shared Compose UI
    ├── ForgotPasswordScreen.kt # Shared Compose UI
    ├── OtpDialog.kt            # Shared Compose UI
    └── AuthManager.kt          # expect contract (thin SDK wrapper)

androidMain/
  └── AuthManager.android.kt   # actual → Clerk Android SDK

iosMain/
  ├── AuthManager.ios.kt       # actual → Clerk iOS SDK (via interop)
  └── ClerkBridge.swift        # Swift wrapper over Clerk iOS SDK
```

## What Is Already Shared (ZERO iOS Changes Needed)

| Artifact | Responsibility |
|----------|---------------|
| UI screens (4) | Compose UI — sign-in, sign-up, forgot password, OTP dialog |
| AuthViewModel | Business logic, state management, event emission |
| AuthEvent / AuthUiState | Sealed interfaces for navigation and UI state |
| AuthValidator | Input validation (email, password, OTP) |
| Koin DI | Wires AuthManager + AuthViewModel |

**You do not touch any of these files for iOS.**
No Compose code, no ViewModel logic, no navigation wiring needs to be duplicated.

## The Only iOS Work: Implement AuthManager.ios.kt

File: `composeApp/src/iosMain/kotlin/.../feature/auth/AuthManager.ios.kt`

Currently 8 empty stubs — every method must call the real Clerk iOS SDK via the Swift wrapper.

## Required: ClerkBridge.swift

Create `composeApp/src/iosMain/kotlin/.../feature/auth/ClerkBridge.swift`

A thin `@objc`-exposed Swift class that wraps Clerk iOS SDK calls. Add it to your Xcode project under the ComposeApp framework target.

```swift
import Clerk

@objc public class ClerkBridge: NSObject {
    @objc public static let shared = ClerkBridge()
    private let clerk = Clerk.shared

    @objc public func signUp(
        email: String,
        password: String,
        completion: @escaping (Error?) -> Void
    ) {
        clerk.signUp.create(params: ...) { result in
            switch result {
            case .success: completion(nil)
            case .failure(let error): completion(error)
            }
        }
    }

    @objc public func signIn(
        email: String,
        password: String,
        completion: @escaping (Error?) -> Void
    ) { ... }

    @objc public func signOut(
        completion: @escaping (Error?) -> Void
    ) { ... }

    @objc public func isUserSignedIn() -> Bool { ... }
    @objc public func currentUserId() -> String? { ... }
    @objc public func currentUserEmail() -> String? { ... }
    @objc public func startOAuthSession(
        provider: String,  // "google" or "apple"
        completion: @escaping (Error?) -> Void
    ) { ... }
}
```

> **Clerk iOS SDK handles:** session persistence, token refresh, OAuth redirects, `ASWebAuthenticationSession`, cookies, MFA, passkeys, deep links. You do NOT rebuild any of this.

## AuthManager.ios.kt — Thin SDK Wrapper

From Kotlin/Native, call the bridge via `suspendCancellableCoroutine`:

```kotlin
actual class AuthManager actual constructor() {

    actual suspend fun signUp(email: String, password: String) {
        suspendCancellableCoroutine<Unit> { cont ->
            ClerkBridge.shared.signUp(email, password) { error ->
                if (error != null) cont.resumeWithException(error)
                else cont.resume(Unit)
            }
        }
    }

    actual fun observeUser(): Flow<AuthUser?> {
        return callbackFlow {
            // Poll or observe ClerkBridge session state
            // send(AuthUser(id, email)) or null
            awaitClose { /* cleanup */ }
        }
    }

    // verifyEmail, signIn, signOut, forgotPassword,
    // resetPassword, signInWithGoogle — same pattern
}
```

**AuthManager is ONLY a thin SDK wrapper — NO business logic here.**
The ViewModel (shared in commonMain) owns all business logic.

## Build Configuration

In `composeApp/build.gradle.kts`, add `ClerkBridge.swift` to the framework:

```kotlin
listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
        baseName = "ComposeApp"
        isStatic = true
    }
}
```

Add `ClerkBridge.swift` to your Xcode project target. No additional Gradle dependency needed — the Swift file is compiled as part of the Xcode framework build.

Clerk iOS SDK is added via Swift Package Manager in Xcode (File → Add Package → `https://github.com/clerk/clerk-ios`).

## API Key Management

Android: `local.properties` → `BuildConfig.CLERK_PUBLISHABLE_KEY`
iOS: `Info.plist` → `NSBundle.mainBundle.objectForInfoDictionaryKey("CLERK_PUBLISHABLE_KEY")`

## ⚠️ CRITICAL: Do NOT Store Auth Data Manually

Clerk manages sessions, tokens, and persistence on both platforms. Your app only needs:

- `isUserSignedIn()` → Boolean
- `currentUser()` → AuthUser?
- `observeUser()` → Flow<AuthUser?>

**Do NOT save JWTs, refresh tokens, or session tokens yourself.**
**Do NOT manually persist auth state.**
Let Clerk handle the full session lifecycle.

## Recommended: SessionViewModel

For startup session detection, create in commonMain:

```kotlin
sealed interface SessionState {
    data object Loading : SessionState
    data object Authenticated : SessionState
    data object Unauthenticated : SessionState
}

class SessionViewModel(
    private val authManager: AuthManager
) : ViewModel() {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        viewModelScope.launch {
            authManager.observeUser().collect { user ->
                _sessionState.value = if (user != null)
                    SessionState.Authenticated
                else
                    SessionState.Unauthenticated
            }
        }
    }
}
```

Your root navigation checks `sessionState`:
- `Loading` → splash screen
- `Authenticated` → main app
- `Unauthenticated` → auth flow

## SSO Differences

| Provider | Android | iOS |
|----------|---------|-----|
| Google | Clerk OAuth → Chrome Custom Tab | Clerk OAuth → `ASWebAuthenticationSession` |
| Apple | Not available | Clerk OAuth → native Apple Sign-In |

The shared `SignInScreen.kt` already renders an **Apple button**. iOS can wire it to `ClerkBridge.shared.startOAuthSession(provider: "apple")`.

## Why NOT to Use Clerk REST API Directly

Even though it looks simpler initially, you would need to manually implement:

- Session refresh
- Secure token lifecycle
- OAuth redirect handling
- Apple / Google Sign-In flows
- Deep link routing
- Cookie and session persistence
- MFA and passkeys

**You become the auth SDK maintainer.** The Clerk iOS SDK handles all of this for free.

## Implementation Roadmap

```
Step 1: Add Clerk iOS SDK via Swift Package Manager in Xcode
Step 2: Create ClerkBridge.swift (@objc wrapper)
Step 3: Add CLERK_PUBLISHABLE_KEY to Info.plist
Step 4: Implement all 8 methods in AuthManager.ios.kt
Step 5: Create SessionViewModel in commonMain
Step 6: Wire root navigation to SessionState
Step 7: Test full auth flow on iOS
```

## Summary

| Aspect | Where | iOS Work Required? |
|--------|-------|-------------------|
| UI (4 screens) | commonMain | ❌ Zero |
| ViewModel + state | commonMain | ❌ Zero |
| Validation | commonMain | ❌ Zero |
| Navigation | commonMain | ❌ Zero |
| Auth SDK wrapper | iosMain | ✅ ~100 lines Kotlin |
| Swift bridge | iosMain | ✅ ~80 lines Swift |
| Build config | Xcode | ✅ Add SPM package |
| API key | Info.plist | ✅ One entry |
| SessionViewModel | commonMain | ✅ ~30 lines Kotlin |

**Total new code:** ~200 lines across 3 files (`AuthManager.ios.kt`, `ClerkBridge.swift`, `SessionViewModel.kt`).
