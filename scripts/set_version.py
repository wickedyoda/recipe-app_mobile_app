import re, sys

version = sys.argv[1]
file_path = sys.argv[2]

with open(file_path) as f:
    content = f.read()

# Set versionName
content = re.sub(r'versionName = "[^"]*"', f'versionName = "{version}"', content)

# Set versionCode: alpha-1.0.XX → 100XX (e.g. 1.0.12 → 10012)
match = re.match(r'alpha-1\.0\.(\d+)', version)
if match:
    patch = int(match.group(1))
    versionCode = 10000 + patch
else:
    versionCode = 1

content = re.sub(r'versionCode = \d+', f'versionCode = {versionCode}', content)

with open(file_path, 'w') as f:
    f.write(content)

print(f"Updated versionName to {version}, versionCode to {versionCode}")