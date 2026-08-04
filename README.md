# CookieRue Mobile App

## Overview

Android and iOS clients that wrap the CookieRue recipe app in a native WebView/SwiftUI shell. Enter a server URL on first launch — it's saved locally — and the app loads the web interface as a native experience.

## Features

- **Cross-platform**: Android (Kotlin + WebView) and iOS (SwiftUI + WKWebView)
- **Dark theme** matching the web UI (`#0f1115` background, `#f3f3f3` text)
- **Host persistence** — server URL saved locally after first entry
- **Security hardened**:
  - HTTPS-only via `network-security-config.xml` (Android) and `allowsCellularAccess`/`allowsArbitraryLoads` (iOS)
  - `usesCleartextTraffic="false"` — all cleartext HTTP blocked
  - `allowFileAccess = false`, `allowContentAccess = false` — file/content access blocked
  - `shouldOverrideUrlLoading` — cross-host navigation restricted
  - WebView debugging disabled (`setWebChromeClient` without `setAllowFileAccessFromFileURLs`)
- **Build & publish**: GitHub Packages export on release (APK to GHCR, IPA via EAS)

## Quick Start

### Using the app

1. On first open, enter your CookieRue server URL (e.g. `https://192.168.1.100:3000`)
2. Tap **Connect** — the URL is saved locally
3. The web app loads. On subsequent opens, it loads directly.

### Android (Kotlin + WebView)

```bash
cd android && ./gradlew assembleRelease
```

### iOS (SwiftUI + WKWebView)

Requires macOS + Xcode 15+:

```bash
open ios/CookieRue.xcodeproj
```

Or build via EAS (requires Expo / Apple credentials):

```bash
eas build --platform ios --token $EXPO_TOKEN
```

## Project Structure

```
recipe-app_mobile_app/
├── android/
│   ├── app/
│   │   ├── src/main/java/com/cookierue/webview/MainActivity.kt
│   │   ├── src/main/AndroidManifest.xml
│   │   └── src/main/res/xml/network_security_config.xml
│   └── build.gradle
├── ios/
│   ├── CookieRue/
│   │   ├── CookieRueApp.swift
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

On release, both clients are published to GitHub Packages:

- **Android APK** → uploaded as release asset + 30-day artifact retention
- **iOS IPA** → uploaded as release asset + 30-day artifact retention (requires `EXPO_TOKEN` secret)

Trigger a release:

```bash
gh release create v1.0.0 --title "v1.0.0" --notes "First mobile release" --repo wickedyoda/recipe-app_mobile_app
```

## License

MIT — see [LICENSE](LICENSE)
