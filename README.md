# Pay-Home

An Android application for home utility payments using M-Pesa integration.

## Overview

Pay-Home is a mobile application that allows users to make utility payments through M-Pesa API integration. The app supports various utility payment types and provides a seamless payment experience.

## Features

- **M-Pesa Integration**: Secure payment processing via Safaricom M-Pesa API
- **Utility Payments**: Support for multiple utility types (electricity, water, etc.)
- **Secure Transactions**: Encrypted API communication
- **User-Friendly Interface**: Simple and intuitive design

## Prerequisites

Before running this project, ensure you have the following installed:

### Required Software
- **Java Development Kit (JDK)**: Version 17 or higher
- **Android Studio**: Latest version recommended
- **Git**: For version control
- **Android SDK**: API level 34 (Android 14)
- **Android Debug Bridge (adb)**: For device communication and log viewing

### Installing Android Debug Bridge (adb)

#### On Ubuntu/Debian:
```bash
# Method 1: Install basic adb
sudo apt update
sudo apt install adb

# Method 2: Install full Android platform tools (recommended)
sudo apt install google-android-platform-tools-installer

# Verify installation
adb version
```

#### On macOS (using Homebrew):
```bash
brew install android-platform-tools
```

#### On Windows:
1. Download [Android SDK Platform Tools](https://developer.android.com/studio/releases/platform-tools)
2. Extract ZIP file
3. Add platform-tools directory to PATH
4. Restart terminal

### System Requirements
- **Operating System**: Windows 10+, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: Minimum 8GB (16GB recommended)
- **Storage**: At least 5GB free space

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/bran259/Pay-Home.git
cd Pay-Home
```

### 2. Install Java JDK

#### On Ubuntu/Debian:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
```

#### On macOS (using Homebrew):
```bash
brew install openjdk@17
export JAVA_HOME=/usr/local/opt/openjdk@17
echo 'export JAVA_HOME=/usr/local/opt/openjdk@17' >> ~/.zshrc
```

#### On Windows:
1. Download and install JDK 17 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Set JAVA_HOME environment variable
3. Add Java bin directory to PATH

### 3. Setup Android Studio

1. Download and install [Android Studio](https://developer.android.com/studio)
2. Open Android Studio
3. Select "Open an existing project"
4. Navigate to the cloned `Pay-Home` directory
5. Wait for Gradle synchronization to complete

### 4. Configure Android SDK

1. Open Android Studio SDK Manager (Tools → SDK Manager)
2. Install the following components:
   - Android SDK Platform 34 (Android 14)
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools
   - Android SDK Tools

### 5. Setup Local Properties

Create a `local.properties` file in the project root:

```properties
sdk.dir=/path/to/your/android/sdk
```

Replace `/path/to/your/android/sdk` with your actual Android SDK path.

## Running the Application

### Method 1: Using Android Studio

1. Open the project in Android Studio
2. Connect an Android device or start an emulator
3. Select the device from the dropdown menu
4. Click the "Run" button (green play icon) or press `Shift + F10`

### Method 2: Using Command Line

1. Open terminal in the project root
2. Ensure your device is connected or emulator is running
3. Run the following commands:

```bash
# Clean the project
./gradlew clean

# Build the project
./gradlew build

# Install and run the app
./gradlew installDebug
```

### Method 3: Using Gradle Wrapper

```bash
# For Linux/macOS
./gradlew assembleDebug

# For Windows
gradlew.bat assembleDebug

# Install the APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Configuration

### M-Pesa API Setup

To use the M-Pesa integration, you need to configure your API credentials:

1. Open `app/src/main/java/MpesaApi/MpesaApi.java`
2. Update the following constants with your actual M-Pesa credentials:
   ```java
   private static final String SHORTCODE = "YOUR_SHORTCODE";
   private static final String CONSUMER_KEY = "YOUR_CONSUMER_KEY";
   private static final String CONSUMER_SECRET = "YOUR_CONSUMER_SECRET";
   private static final String PHONE_NUMBER = "YOUR_PHONE_NUMBER";
   private static final String CALLBACK_URL = "YOUR_CALLBACK_URL";
   ```

### Getting M-Pesa Credentials

1. Register on the [Safaricom Developer Portal](https://developer.safaricom.co.ke/)
2. Create a new app
3. Obtain your Consumer Key and Consumer Secret
4. Set up your callback URL for payment notifications

## Project Structure

```
Pay-Home/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/payhome/
│   │   │   │   ├── payhome.java          # Main activity
│   │   │   │   └── MpesaApi/
│   │   │   │       └── MpesaApi.java      # M-Pesa API integration
│   │   │   ├── res/                       # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml        # App manifest
│   │   └── build.gradle.kts               # App-level build configuration
│   └── build.gradle.kts                   # Project-level build configuration
├── gradle/
│   └── libs.versions.toml                 # Version catalog
├── build.gradle.kts                       # Root build configuration
├── settings.gradle.kts                    # Gradle settings
└── local.properties                       # Local configuration (gitignored)
```

## Dependencies

The project uses the following main dependencies:

- **AndroidX**: Modern Android support libraries
- **Material Design**: UI components
- **Retrofit2**: HTTP client for API calls
- **OkHttp3**: HTTP client with logging
- **Gson**: JSON parsing
- **JUnit**: Unit testing
- **Espresso**: UI testing

## Build Variants

The project supports two build variants:

- **debug**: Development version with debugging enabled
- **release**: Production version with optimizations

## Testing

### Running Unit Tests
```bash
./gradlew test
```

### Running Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Viewing Project Output

### APK Location
After building successfully, your APK is located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Installation Methods

#### Method 1: Using ADB (Android Debug Bridge)
```bash
# Install APK to connected device/emulator
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall app
adb uninstall com.example.payhome
```

#### Method 2: Using Gradle
```bash
# Install debug APK
./gradlew installDebug

# Install release APK
./gradlew installRelease
```

#### Method 3: Manual Installation
1. Copy `app-debug.apk` to your Android device
2. Enable "Install from unknown sources" in device settings
3. Tap on the APK file to install

### Runtime Output (Logcat)

#### Method 1: Using Android Studio
1. Open Android Studio
2. Click on "Logcat" tab at the bottom
3. Select your device and app package (`com.example.payhome`)
4. Filter by tag or log level

#### Method 2: Using Command Line
```bash
# View all logs
adb logcat

# Filter by your app package
adb logcat | grep "com.example.payhome"

# Filter by specific tags
adb logcat -s "MainActivity" "MpesaApi"

# Clear log buffer
adb logcat -c

# Save logs to file
adb logcat > app_logs.txt
```

#### Method 3: Adding Logging to Your Code
```java
// In your Java files
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private void processPayment(String phoneNumber, String password) {
        Log.d(TAG, "Processing payment for: " + phoneNumber);
        
        if (password.isEmpty()) {
            Log.e(TAG, "Password is empty");
            return;
        }
        
        Log.i(TAG, "Payment validation successful");
    }
}
```

### Build Output Locations

#### APK Files
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`
- **Metadata**: `app/build/outputs/apk/debug/output-metadata.json`

#### Build Logs
- **Gradle logs**: `app/build/outputs/logs/`
- **Compilation logs**: View in terminal or Android Studio Build tab

#### Generated Files
- **Generated sources**: `app/build/generated/`
- **Intermediate files**: `app/build/intermediates/`
- **Temporary files**: `app/build/tmp/`

### Testing Your App

#### 1. Run on Emulator
```bash
# Start Android emulator
emulator -avd <avd_name>

# Install and run
./gradlew installDebug
adb shell am start -n com.example.payhome/.MainActivity
```

#### 2. Run on Physical Device
```bash
# Connect device via USB
# Enable USB debugging in device settings
adb devices

# Install app
./gradlew installDebug

# Launch app
adb shell am start -n com.example.payhome/.MainActivity
```

### Debugging Tips

#### 1. Check App Status
```bash
# Check if app is installed
adb shell pm list packages | grep "com.example.payhome"

# Check app info
adb shell dumpsys package com.example.payhome
```

#### 2. Monitor App Performance
```bash
# Monitor CPU usage
adb shell top | grep "com.example.payhome"

# Monitor memory usage
adb shell dumpsys meminfo com.example.payhome
```

#### 3. Common Logcat Filters
```bash
# Show only errors and warnings
adb logcat *:E

# Show verbose logs for your app only
adb logcat com.example.payhome:V

# Show M-Pesa related logs
adb logcat | grep -i "mpesa\|payment\|transaction"
```

## Troubleshooting

### Common Issues and Solutions

#### 1. "compileSdkVersion is not specified" Error
**Solution**: This should be resolved with the latest updates. If it persists:
- Ensure `local.properties` exists with correct SDK path
- Clean and rebuild the project: `./gradlew clean && ./gradlew build`

#### 2. "JAVA_HOME is not set" Error
**Solution**: 
- Install JDK 17 or higher
- Set JAVA_HOME environment variable
- For Ubuntu: `export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`

#### 3. Gradle Sync Failure
**Solution**:
- Check internet connection
- Update Android Studio and Gradle wrapper
- Invalidate caches: File → Invalidate Caches/Restart

#### 4. M-Pesa API Connection Issues
**Solution**:
- Verify API credentials are correct
- Check if callback URL is accessible
- Ensure you're using sandbox credentials for testing

#### 5. Build Errors
**Solution**:
- Clean project: `./gradlew clean`
- Rebuild project: `./gradlew build`
- Check Android SDK installation

### Getting Help

If you encounter issues not covered here:

1. Check the [Android Studio documentation](https://developer.android.com/studio)
2. Review [M-Pesa API documentation](https://developer.safaricom.co.ke/)
3. Check the project issues on GitHub
4. Ensure all prerequisites are properly installed

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Commit changes: `git commit -m 'Add feature description'`
5. Push to branch: `git push origin feature-name`
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For questions or support, please create an issue on the GitHub repository.