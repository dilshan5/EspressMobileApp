# EspressMobileApp
This is an example for Mobile test automation using Espresso

# Prerequisites
This requires [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) v11

## Open the Project
1. Check out the code
2. Build the project
3. If you are using Unified Test Platform (UTP), no need to set up the Emulators, if not set up your Mobile Device/Emulator
4. Set your Geo apiKey at `local.properties`
5. Integration Tests can be found at `app/src/androidTest/java/app/com/mobileassignment/test/InstrumentedIntegrationTest.kt`
6. E2E Tests can be found at `app/src/androidTest/java/app/com/mobileassignment/test/InstrumentedE2ETest.kt`
7. If you need to execute both Integration and E2E tests together, you can run the Regression Test Suite at `app.com.mobileassignment.suites.EspressoRegressionSuite`


##  Unified Test Platform - [UTP](https://www.youtube.com/watch?v=juEkViDyzF8)
An extensible test executor for running Android tests at scale from Android Studio and the Android Gradle plugin.

Open `app/build.gradle` and under `devices` section add your emulator details as you need.

For more details, please [visit](https://android-developers.googleblog.com/2021/10/whats-new-in-scalable-automated-testing.html)

## Running Tests (via terminal)

- Regression test  - `./gradlew connectedAndroidTest `

## Running Tests (via Unified Test Platform)

Use Gradle Managed Virtual devices - `/gradlew -Pandroid.experimental.androidTest.useUnifiedTestPlatform=true pixel2api29DebugAndroidTest`

## Bonus Question

The issue was with the API key. You need to place your API key in `local.properties` file.
I have stored the API key in a secured way.  DO NOT commit the key.
