# CookieRue iOS Client

SwiftUI + WKWebView wrapper for the CookieRue recipe app web interface.

## Structure

```
ios/
  CookieRue/
    CookieRueApp.swift       # App entry point (@main)
    ContentView.swift        # Host entry UI + connection state
    RecipeAppWebView.swift   # WKWebView UIViewRepresentable
```

## Build

### Local (macOS + Xcode 15+)

```bash
open ios/CookieRue.xcodeproj
```

### Cloud Build (CI)

Uses [EAS CLI](https://docs.expo.dev/build/introduction/) (configured in `.github/workflows/mobile-build.yml`):

```bash
eas build --platform ios --token $EXPO_TOKEN
```

On first launch:
1. Enter your CookieRue server URL
2. Tap **Connect** — URL is saved to `AppStorage`
3. Web app loads inside `WKWebView`

## Security

- `WKWebView` with `isOpaque = false` (transparent background)
- JavaScript and media playback enabled for web app functionality
- No local file access exposed to web content
- Network security config enforced via app-level settings
