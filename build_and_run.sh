#!/bin/bash

# Check if a parameter is provided
if [ -z "$1" ]; then
    echo "Usage: $0 {desktop|android|web}"
    exit 1
fi

# Set execution mode based on the argument
case "$1" in
    desktop)
        ./gradlew :composeApp:run
        ;;
    android)
        ./gradlew :composeApp:assembleDebug && ./gradlew :composeApp:installDebug
        ;;
    web)
        ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
        ;;
    *)
        echo "Invalid parameter: $1"
        echo "Usage: $0 {desktop|android|web}"
        exit 1
        ;;
esac