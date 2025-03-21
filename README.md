# Battleship game

- The game begins with view for the first player and only one grid is shown since the second player
- has not places his ships. The placement of a ship is done with selecting the first coordinate of the grid 
- and the last coordinate. The available ships are shown on the right side of the view.
# Setup
- Version for Java JDK must be 17 <= 22. 
## Linux and MacOs
- For building and running the app all you to do is is set first JAVA_HOME environment variable if is not 
- set give permission to `build_and_run.sh` script and run it with parameter `desktop|android|web` 
- i did not have the chance to test for ios so is excluded from this script.

## Windows
- Run `build_and_run.ps1` from power shell. 

# Code
- The code is located at `composeApp/src/commonMain` and the tests `composeApp/src/commonTest`

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.