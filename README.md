<div align="center">
  <img src="https://github.com/wickedyoda/recipe-app/raw/master/frontend/src/icons/logo-lg.png" alt="WiskFul logo" width="120" height="120" />
</div>

<div align="center">

## WiskFul
> **Plan it. Cook it. Love it.**

</div>

## Overview

Android (primary) client that wraps the WiskFul recipe app in a native WebView shell. Enter a server URL on first launch — it's saved locally — and the app loads the web interface as a native experience.

## Features

- **Mobile-first**: Android client with native WebView shell
- **Dark theme** matching the web UI (`#0f1115` background)
- **Edge-to-edge layout**: WebView fills behind status bar, no top padding
- **Host persistence** — server URL saved locally after first entry
- **Security hardened**:
  - HTTPS-only via `network-security-config.xml` (Android)
  - `usesCleartextTraffic="false"` — all cleartext HTTP blocked
  - `allowFileAccess = false`, `allowContentAccess = false` — file/content access blocked
  - `shouldOverrideUrlLoading` — cross-host navigation restricted
  - WebView debugging disabled
- **App icons**: Teal (#0c6160) background with logo at all densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
- **Build & publish**: APK auto-built and uploaded on every push to `main` (90-day retention)

## Quick Start

### Using the app

1. On first open, enter your WiskFul server URL (e.g. `https://192.168.1.100:3000`)
2. Tap **Connect** — the URL is saved locally
3. The web app loads. On subsequent opens, it loads directly.

### Building the APK

#### Prerequisites
- Android SDK 35+
- JDK 17
- Android Studio or command-line tools

```bash
cd android && ./gradlew assembleDebug
```

**Warning:** Build is workflow_dispatch only — APKs are built via GitHub Actions. Local builds require the release keystore credentials:

```bash
# For signed debug APK (enables upgrade from previous versions)
SIGNING_STORE_FILE=/path/to/keystore \
SIGNING_STORE_PASSWORD=*** \
SIGNING_KEY_ALIAS=release \
SIGNING_KEY_PASSWORD=*** \
./gradlew assembleDebug
```

Debug APK is built via GitHub Actions (`workflow_dispatch` only) — find releases at: https://github.com/wickedyoda/recipe-app_mobile_app/releases

### iOS — Not currently supported

iOS development is **paused**. The iOS code in `ios/WhiskFul/` is preserved but will not be actively developed. Android is the primary mobile client.

## Project Structure

```
recipe-app_mobile_app/
├── android/
│   ├── app/src/main/java/com/whiskful/webview/MainActivity.kt
│   ├── app/src/main/AndroidManifest.xml
│   ├── app/src/main/res/values/themes.xml      # Dark theme (#0f1115)
│   ├── app/src/main/res/values/colors.xml      # Color resources
│   ├── app/src/main/res/values/strings.xml     # App title, labels
│   ├── app/src/main/res/mipmap-*/ic_launcher.png  # App icons
│   └── app/src/main/res/xml/network_security_config.xml
├── ios/                    # iOS client (paused)
│   ├── WhiskFul/
│   │   ├── WhiskFulApp.swift
│   │   ├── ContentView.swift
│   │   └── RecipeAppWebView.swift
│   └── README.md
├── scripts/
│   └── set_version.py      # Auto-increment version name
├── security-reports/       # Generated security scan reports
├── LICENSE
└── README.md
```

## Security Checks

| Check | Tool | Scope | Continue-on-error |
|-------|------|-------|-------------------|
| Kotlin Lint | ktlint | Android code style | No |
| Secrets | Manual scan | API keys, credentials | No |
| WebView Security | Manual | file access, network | No |
| Signing | Manual | Key rotation required | N/A |

## GitHub Actions Workflow

The workflow (`mobile-apk-release.yml`) runs:

1. **Version bump**: Computes next alpha version (`alpha-1.0.XX`)
2. **Java setup**: JDK 17
3. **Sign keystore**: Optional (from secrets)
4. **Build APKs**: Debug and Release
5. **Create Release**: With auto-generated notes
6. **Upload Artifacts**: 90-day retention

**Manual build only**: Trigger via `workflow_dispatch` on GitHub UI — no push triggers.

## Updating

### Weekly upstream sync (for build tools)

```bash
git fetch origin
git rebase origin/main
```

### For critical dependencies

Check `mobile-security.yml` for security scan configuration.

## License

MIT — see [LICENSE](LICENSE)

## Security Reports

Past security scans stored in `security-reports/`:
- `security-scan-YYYY-MM-DD.md` — latest scan results