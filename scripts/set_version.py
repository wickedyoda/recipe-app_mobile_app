import re, sys

version = sys.argv[1]
file_path = sys.argv[2]

with open(file_path) as f:
    content = f.read()

content = re.sub(r'versionName = "[^"]*"', f'versionName = "{version}"', content)

with open(file_path, 'w') as f:
    f.write(content)

print(f"Updated versionName to {version}")
