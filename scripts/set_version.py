import re, sys
version = sys.argv[1] if len(sys.argv) > 1 else "unknown"
gradle_file = sys.argv[2] if len(sys.argv) > 2 else "app/build.gradle.kts"

with open(gradle_file) as f:
    content = f.read()

# Use a more flexible pattern that handles whitespace
pattern = r'versionName\s*=\s*"[^"]*"'
replacement = f'versionName = "{version}"'

new_content = re.sub(pattern, replacement, content)

if new_content == content:
    # Try alternate pattern
    print(f"Pattern 1 didn't match, trying alternate...")
    pattern = r'versionName=.*"[^"]*"'
    new_content = re.sub(pattern, replacement, content)
    if new_content == content:
        print(f"ERROR: No versionName line found")
        print(f"File content preview: {content[content.find('versionName')-50:content.find('versionName')+50] if 'versionName' in content else 'not found'}")
        sys.exit(1)

with open(gradle_file, "w") as f:
    f.write(new_content)
print(f"Updated versionName to {version}")