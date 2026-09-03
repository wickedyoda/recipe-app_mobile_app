import re, sys

version = sys.argv[1]
file_path = sys.argv[2]

with open(file_path) as f:
    content = f.read()

# Convert alpha-1.0.0X to numeric versionCode (e.g., alpha-1.0.09 -> 1009)
# This ensures versionCode always increments with versionName
match = re.match(r'alpha-1\.0\.(\d+)', version)
if match:
    patch = int(match.group(1))
    versionCode = 10000 + patch
else:
    versionCode = 1

content = re.sub(r'versionName = "[^"]*"', f'versionName = "{version}"', content)
content = re.sub(r'versionCode = \d+', f'versionCode = {versionCode}', content)

with open(file_path, 'w') as f:
    f.write(content)

print(f"Updated versionName to {version}, versionCode to {versionCode}")
