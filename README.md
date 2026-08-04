# CookieRue Mobile App

## Overview

Android and iOS clients that wrap the CookieRue web app in a native WebView/SwiftUI shell. Enter a server URL on first launch — it's saved locally — and the app loads the web interface so it behaves like a native experience.

## Features

- Dark theme matching the web UI (`#0f1115` background, `#f3f3f3` text)
- Host persistence — server URL saved locally after first entry
- Security hardened — HTTPS-only, cleartext traffic disabled, file/content access blocked, cross-host navigation restricted
- Native wrappers — Android uses `WebView`, iOS uses `WKWebView` behind `UIViewRepresentable`

## Quick Start

### Using the app

1. On first open, enter your server URL (e.g. `https://192.168.1.100:3000`)
2. Tap **Connect** — the URL is saved locally
3. The web app loads. On subsequent opens, it loads directly.

### Android

```
cd android && ./gradlew assembleRelease
```

### iOS (macOS only)

Open `ios/CookieRue.xcodeproj` in Xcode and run.

## Security Checks

Runs on every PR via GitHub Actions:

| Check | Tool | Scope |
|-------|------|-------|
| Kotlin Lint | ktlint | Android code style |
| SAST | CodeQL | Kotlin/Java security issues |
| Secrets | TruffleHog | Verified secrets in history |
| Container | Trivy | Filesystem vulnerabilities |
| Assets | Custom | Manifest, network config, iOS files |
| YAML | yamllint | Config file validity |

## License

MIT — see [LICENSE](LICENSE)