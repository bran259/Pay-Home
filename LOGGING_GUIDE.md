# Pay-Home Logging Guide

## Quick Logcat Commands

### Filter by Your App Only
```bash
# Show all logs from your app
adb logcat | grep "com.example.payhome"

# Alternative method
adb logcat --pid=$(adb shell pidof com.example.payhome)
```

### Filter by Specific Tags
```bash
# Show MainActivity logs
adb logcat -s "PayHomeMainActivity"

# Show MpesaApi logs
adb logcat -s "MpesaApi"

# Show both tags
adb logcat -s "PayHomeMainActivity:MpesaApi"
```

### Filter by Log Level
```bash
# Show only errors and warnings
adb logcat *:W | grep "com.example.payhome"

# Show verbose logs for your app
adb logcat com.example.payhome:V
```

### Common Useful Filters
```bash
# Payment related logs
adb logcat | grep -i "payment\|mpesa\|transaction"

# Error logs only
adb logcat | grep -E "(ERROR|FATAL)" | grep "com.example.payhome"

# Real-time monitoring
adb logcat | grep "PayHomeMainActivity\|MpesaApi"
```

## What You'll See

### MainActivity Logs (Tag: PayHomeMainActivity)
- `MainActivity onCreate started` - App initialization
- `Layout set successfully` - UI loaded
- `Payment button clicked` - User interaction
- `Processing payment for phone: xxx` - Payment attempt
- `Phone number validated successfully` - Validation passed
- `Payment successful - sending confirmation SMS` - Success
- `Payment failed - M-Pesa transaction unsuccessful` - Failure

### MpesaApi Logs (Tag: MpesaApi)
- `Requesting M-Pesa access token` - API authentication start
- `Access token retrieved successfully` - Authentication success
- `Starting M-Pesa payment for Rent amount: 1000` - Payment initiation
- `Failed to get access token. Response: xxx` - API errors

## Testing Your Logs

1. **Build and install the app**:
   ```bash
   ./gradlew assembleDebug
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Start log monitoring**:
   ```bash
   adb logcat | grep "PayHomeMainActivity\|MpesaApi"
   ```

3. **Launch the app and click the payment button**

4. **Watch the logs appear in real-time**

## Troubleshooting

### No logs appearing?
- Check if app is installed: `adb shell pm list packages | grep payhome`
- Try broader filter: `adb logcat | grep -i "payhome"`
- Restart logcat: `adb logcat -c` then try again

### Too many system logs?
- Use specific tag filtering: `adb logcat -s "PayHomeMainActivity"`
- Filter by process: `adb logcat --pid=$(adb shell pidof com.example.payhome)`

### Log levels used:
- `Log.i()` - Info (important events)
- `Log.d()` - Debug (detailed flow)
- `Log.e()` - Error (problems)
- `Log.w()` - Warning (validation issues)
