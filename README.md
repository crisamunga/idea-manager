# IDEA Manager

Native Android Project using Kotlin showcasing

- UI development using xml views
- Room database
- Work manager
- Internationalization
- Dark theme support
- Biometric security integration
- AndroidX Preferences

## Requirements

This project was built with:

1. Android Studio Ladybug Feature Drop | 2024.2.2
2. Jetbrains Java JDK 21 (Integrated in Android Studio)
3. Gradle 8.10.1
4. Kotlin 2.1.0

## UI implementation

The UI is implemented using XML views. XML views were chosen as I can develop much faster with them as compared to Compose, even though Compose is better for performance, 
reusability and customization. Compose also avoids some of the restrictions that XML has like dynamic styling of components at runtime.

There are two screens in the app, the home page which lists tasks and the settings page. The addition/editing and deleting of tasks are implemented using a bottom sheet.

Jetpack Navigation is used to implement navigation between the home page and the settings page. The app uses a single activity architecture with multiple fragments. 
The main activity hosts the toolbar and sets the title and menu based on the destination.

## Room database

The app uses Room database to store tasks. The database is implemented using a singleton pattern to ensure that only one instance of the database is created. 
The database is accessed using a repository pattern to separate the data layer from the UI layer.

The app uses coroutines and flows to implement asynchronous programming. The app uses coroutines to perform background tasks and flows to observe changes in the database.

## Work manager

The app uses WorkManager to schedule notifications for tasks that are due that day. The work is scheduled on app launch and is repeated every day. 
The work is scheduled using a periodic work request that is set to repeat every 24 hours, and starts with an offset to aim at 8 AM.

## Internationalization

The app supports internationalization by using string resources. The app is currently available in English and French. The app detects the device language and sets the app language accordingly. 
The app also supports app specific language settings, which can be changed in the in app settings page or in the app's settings page in the OS system settings menu in newer Android versions.

## Dark theme support

The app supports dark theme by using the AppCompat library. The app uses the AppCompatActivity class to support dark theme. The app also supports dynamic theming, which means that the app can change the theme at runtime based on the system theme.

## Biometric security integration

The app uses the AndroidX Biometric library to implement biometric security. The user using fingerprint or face recognition when they leave and return to the app or when it's first launched.

## AndroidX Preferences

The app uses the AndroidX Preferences library to implement the settings page. The settings page is implemented using a preference fragment that displays a list of preferences.
Additional actions are taken when the country is changed, and when biometric authentication is enabled.
