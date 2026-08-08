<div align="center">
  <img src="https://github.com/wickedyoda/recipe-app/raw/master/frontend/src/icons/logo-lg.png" alt="WiskfFul logo" width="120" height="120" />
</div>

<div align="center">

## WiskfFul

> **Plan it. Cook it. Love it.**

</div>

## Overview

Android (primary) and iOS clients that wrap the WiskfFul recipe app in a native WebView/SwiftUI shell. Enter a server URL on first launch — it's saved locally — and the app loads the web interface as a native experience.

## Features

- **Mobile-first**: Android (primary), iOS (secondary) clients
- **Dark theme** matching the web UI (`#0f1115` background, `#f3f3f3` text)
- **Host persistence** — server URL saved locally after first entry
- **Security hardened**:
  - HTTPS-only via `network-security-config.xml` (Android)
  - `usesCleartextTraffic="false"` — all cleartext HTTP blocked
  - `allowFileAccess = false`, `allowContentAccess = false` — file/content access blocked
  - `shouldOverrideUrlLoading` — cross-host navigation restricted
  - WebView debugging disabled
- **Build & publish**: APK auto-built and uploaded on every push to `main` (90-day retention)

## Quick Start

### Using the app

1. On first open, enter your WiskfFul server URL (e.g. `https://192.168.1.100:3000`)
2. Tap **Connect** — the URL is saved locally
3. The web app loads. On subsequent opens, it loads directly.

### Android (Kotlin + WebView)

```bash
cd android && ./gradlew assembleDebug
```

Debug APK is **auto-built on every push** to `main` via GitHub Actions and attached to a prerelease GitHub Release tagged `apk-{commit-sha}`. Download from: https://github.com/wickedyoda/recipe-app_mobile_app/releases

### iOS (SwiftUI + WKWebView) — paused

iOS development is **temporarily paused**. Android will be the primary mobile client first. The iOS code in `ios/WiskfFul/` is preserved and will be resumed in a future update.

To build iOS later (requires macOS + Xcode 15+):

```bash
open ios/WiskfFul.xcodeproj
```

Or via EAS (requires Expo / Apple credentials):

```bash
eas build --platform ios --token $EXPO_TOKEN
```

## Project Structure

```
recipe-app_mobile_app/
├── android/
│   ├── app/
│   │   ├── src/main/java/com/wiskfful/webview/MainActivity.kt
│   │   ├── src/main/AndroidManifest.xml
│   │   └── src/main/res/xml/network_security_config.xml
│   └── build.gradle
├── ios/
│   ├── WiskfFul/           # iOS client (paused — will resume in future)
│   │   ├── WiskfFulApp.swift
│   │   ├── ContentView.swift
│   │   └── RecipeAppWebView.swift
│   └── README.md
├── .github/workflows/mobile-build.yml
├── LICENSE
└── README.md
```

## Security Checks

Runs on every push, PR, and release via [GitHub Actions](.github/workflows/mobile-build.yml):

| Check | Tool | Scope | Continue-on-error |
|-------|------|-------|-------------------|
| Kotlin Lint | ktlint | Android code style | No |
| SAST | CodeQL | Kotlin/Java security issues | Yes (no buildable project) |
| Secrets | TruffleHog | Verified secrets in history | No |
| Container | Trivy | Filesystem vulnerabilities | No |
| Assets | Custom | Manifest, network config, iOS files | No |
| YAML | yamllint | Config file validity | No |

> SAST uses `continue-on-error: true` because CodeQL requires a full compilable Gradle project. The Kotlin source files are raw WebView wrappers. To fully enable SAST, scaffold a proper `build.gradle` with Kotlin DSL in `android/`.

## GitHub Packages Export

- **Android APK** → auto-built on every push to `main`, uploaded as a prerelease GitHub Release (90-day retention)

## License

MIT — see [LICENSE](LICENSE)