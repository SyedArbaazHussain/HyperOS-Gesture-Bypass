#!/bin/bash
# Path to the module properties file
PROP_FILE="module.prop"

# Extract the current versionCode
OLD_VERSION=$(grep 'versionCode=' $PROP_FILE | cut -d= -f2)

# Increment the version code by 1
NEW_VERSION=$((OLD_VERSION + 1))

# Update the versionCode in the file
sed -i "s/versionCode=.*/versionCode=$NEW_VERSION/" $PROP_FILE

# If a tag name was passed as an argument (e.g. v1.1), update the version string
if [ -n "$1" ]; then
    sed -i "s/version=.*/version=$1/" $PROP_FILE
fi

echo "Updated $PROP_FILE: versionCode $OLD_VERSION -> $NEW_VERSION"