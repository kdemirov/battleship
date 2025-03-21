param (
    [string]$platform
)

if (-not $platform) {
    Write-Host "Usage: build_and_run.ps1 {desktop|android|ios|web}"
    exit 1
}

switch ($platform) {
    "desktop" {
        ./gradlew :composeApp:run
    }
    "android" {
        ./gradlew :composeApp:assembleDebug
        ./gradlew :composeApp:installDebug
    }
    "web" {
        ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    }
    default {
        Write-Host "Invalid parameter: $platform"
        Write-Host "Usage: build_and_run.ps1 {desktop|android|ios|web}"
        exit 1
    }
}