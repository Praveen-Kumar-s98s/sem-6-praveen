# Sentinel X - Digital Arrest Scam Detector

An offline Android application that detects "Digital Arrest" scams using behavioral heuristics without reading private message content.

## 🎯 Features

- **🧠 Behavioral Risk Engine**: 40/20/20/20 point scoring system
- **🛡️ Real-time Monitoring**: Accessibility Service-based detection
- **🚨 Cognitive Friction Barrier**: 30-second unskippable warning with haptic feedback
- **🕶️ OLED Stealth UI**: Premium dark theme with Matrix Green accents
- **🔒 Complete Privacy**: Zero internet permissions, all processing on-device

## 📱 Download APK

### Option 1: GitHub Releases
Go to [Releases](https://github.com/Praveen-Kumar-s98s/sem-6-praveen/releases) and download the latest `app-debug.apk`

### Option 2: GitHub Actions Artifacts
1. Go to [Actions](https://github.com/Praveen-Kumar-s98s/sem-6-praveen/actions)
2. Click on the latest successful build
3. Download `sentinel-x-debug` artifact

### Option 3: Build Locally
```bash
git clone https://github.com/Praveen-Kumar-s98s/sem-6-praveen.git
cd sem-6-praveen
./gradlew assembleDebug
```
APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## 🛠️ How It Works

### Behavioral Detection System
1. **Rapid Transition (40 pts)**: Detects switches from WhatsApp/Telegram to Banking apps in < 8 seconds
2. **Session Persistence (20 pts)**: Flags long calls (>10 mins) while accessing financial apps
3. **Pressure Cues (20 pts)**: Scans for keywords like "Arrest", "CBI", "Police", "FIR"
4. **Panic Interaction (20 pts)**: Detects rapid tapping and settings navigation

When risk score ≥ 75, the app displays a full-screen warning with a 30-second countdown.

## 📦 Installation

1. Download `app-debug.apk`
2. Enable "Install from Unknown Sources" on your Android device
3. Install the APK
4. Grant required permissions:
   - **Accessibility Service**: Settings → Accessibility → Sentinel X → Enable
   - **Display over other apps**: Settings → Apps → Sentinel X → Display over other apps → Allow
   - **Usage Stats**: Settings → Apps → Special Access → Usage Access → Sentinel X → Allow

## 🏗️ Project Structure

```
app/src/main/java/com/sentinelx/
├── engine/
│   └── RiskEngine.java          # Behavioral scoring logic
├── monitoring/
│   └── SentinelService.java     # Accessibility service
└── ui/
    ├── MainActivity.java         # Dashboard
    └── WarningOverlay.java       # Warning screen
```

## 🔒 Privacy & Security

- ✅ **No Internet Permission**: Completely offline
- ✅ **No Data Collection**: All processing on-device
- ✅ **No Message Reading**: Only scans visible UI elements
- ✅ **Open Source**: Full code transparency

## 🎓 Academic Context

This project demonstrates:
- **Behavioral Heuristics** over signature-based detection
- **Human Factor** in cybersecurity
- **Cognitive Friction** as a defense mechanism
- **Privacy-preserving** threat detection

## 📄 License

MIT License - See LICENSE file for details

## 👨‍💻 Author

Praveen Kumar - Semester 6 Project

## 🙏 Acknowledgments

Built as part of academic research on combating social engineering attacks targeting vulnerable populations in India.
