import re, sys
version = sys.argv[1] if len(sys.argv) > 1 else "unknown"
gradle_file = sys.argv[2] if len(sys.argv) > 2 else "android/app/build.gradle.kts"
with open(gradle_file) as f:
    content = f.read()
pattern = r'versionName = "[^"]*"'
replacement = f'versionName = "{version}"'
new_content = re.sub(pattern, replacement, content)
if new_content == content:
    print(f"ERROR: No versionName line found")
    sys.exit(1)
with open(gradle_file, "w") as f:
    f.write(new_content)
print(f"Updated versionName to {version}")
