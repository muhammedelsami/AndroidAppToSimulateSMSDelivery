# AndroidAppToSimulateSMSDelivery

An Android application that demonstrates SMS sending and receiving capabilities. The app is built using Kotlin and provides a simple interface for SMS operations.

## Features

- Send predefined SMS messages
- Receive and display incoming SMS messages
- Runtime permission handling for SMS operations
- Real-time SMS delivery status updates

## Technical Details

- Built with Kotlin
- Targets modern Android API levels
- Uses Android's SMS APIs (SmsManager, BroadcastReceiver)
- Implements proper permission handling

## Usage

The app requires SMS permissions to function properly. Upon first launch, it will request:
- SEND_SMS
- RECEIVE_SMS
- READ_SMS

Once permissions are granted, you can:
1. Use the send button to dispatch a test SMS
2. View received messages in the main screen