# WhiskFul iOS Client

SwiftUI + WKWebView wrapper for the WhiskFul recipe app web interface.

## Structure

```
ios/
  WhiskFul/
    WhiskFulApp.swift          # App entry point (@main)
    ContentView.swift          # Host entry UI + connection state
    RecipeAppWebView.swift     # WKWebView UIViewRepresentable
    AppLogger.swift            # File logging (10MB cap)
    Info.plist                 # App info / CSP settings
    LaunchScreen.storyboard    # Launch screen
    Assets.xcassets/           # Icons (white logo + coral heart on teal)
  WhiskFul.xcodeproj/          # Xcode project
  WhiskFul.xcworkspace/        # Xcode workspace
```

## Features (parity with Android)

- Host entry dialog on first launch (UserDefaults/AppStorage)
- WKWebView with HTTPS-only URL validation
- External navigation blocking (links open in Safari)
- Version badge at bottom-center of screen
- App log file (10MB cap, saved to Documents)
- Dark theme (teal #0c6160, coral #f55b56)

## Build

### Local (macOS + Xcode 15+)
```bash
open ios/WhiskFul.xcworkspace
```
Then press Cmd+R to build and run on simulator or device.

### CI/CD
Builds are manual via [`workflow_dispatch`](.github/workflows/mobile-ipa-release.yml).

```bash
gh workflow run mobile-ipa-release.yml -R wickedyoda/recipe-app_mobile_app
```

On first launch:
1. Enter your WhiskFul server URL
2. Tap **Connect** — URL is saved to `AppStorage`
3. Web app loads inside `WKWebView`

## Security

- `WKWebView` with `isOpaque = false` (transparent background)
- JavaScript and media playback enabled for web app functionality
- No local file access exposed to web content
- Network security config enforced via app-level settings