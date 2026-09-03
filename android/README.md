# Android Build Documentation

## Build Configuration

This document describes the Android build configuration for WiskFul.

### Signing Configuration

The build uses two signing configs:

1. **release** — Signed with the release keystore (when secrets are available)
2. **debugSigned** — Also uses the release keystore for debug builds

This allows seamless upgrades from debug to release without uninstall.

### Build Types

| Type | Minify | Debuggable | Signing |
|------|--------|------------|---------|
| debug | No | Yes | release key (debugSigned) |
| release | No | No | release key |

### Environment Variables

Required for signed releases:
- `SIGNING_STORE_FILE` — Path to `.keystore` file
- `SIGNING_STORE_PASSWORD` — Keystore password
- `SIGNING_KEY_ALIAS` — Key alias
- `SIGNING_KEY_PASSWORD` — Key password

### Version Numbers

Version name format: `alpha-1.0.XX`  
Version code: `100XX` (from version name patch)

Uses `scripts/set_version.py`:
```bash
python3 scripts/set_version.py "alpha-1.0.12" android/app/build.gradle.kts
```

### Dependencies

- `androidx.appcompat:appcompat:1.7.0`
- `androidx.core:core-ktx:1.13.1`

### Security

- `interstoodTraffic="false"` in manifest
- Network security config blocks cleartext
- WebView security hardened in MainActivity.kt

## Manual Builds

### Debug APK (with release key for seamless upgrade)

```bash
SIGNING_STORE_FILE=../whiskful-release.keystore \
SIGNING_STORE_PASSWORD=*** \
SIGNING_KEY_ALIAS=release \
SIGNING_KEY_PASSWORD=*** \
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK

```bash
SIGNING_STORE_FILE=../whiskful-release.keystore \
SIGNING_STORE_PASSWORD=*** \
SIGNING_KEY_ALIAS=release \
SIGNING_KEY_PASSWORD=*** \
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Troubleshooting

### "Different signature" error

This happens when the keystore changes. Users must uninstall before upgrading.

### Version code collision

Use `10#` prefix for base-10 interpretation:
```bash
ver=$(( 10#$(git describe --tags --match "alpha-1.0.*" | cut -d. -f3) + 1 ))
```