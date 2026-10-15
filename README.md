# Aravalli Furniture Android App 🛋️🪑

[![Build & Release Android APK](https://github.com/actions/workflows/build-apk.yml/badge.svg)](https://github.com/actions/workflows/build-apk.yml)

A modern Android application for **Aravalli Furniture & Interiors** built with Kotlin and Jetpack Compose. Includes showroom business management, quotations/estimates generator, confirmed orders tracking, carpenter loyalty club, walk-in showroom visitors CRM, festive greetings cards maker, and live analytics.

---

## 📱 Mobile Phone Pe App Kaise Install Karein (How to Install on Mobile)

### तरीका 1: GitHub Actions से Direct APK Download करके (Sabse Aasan Tarika)

1. Apne GitHub Repository page par jayein.
2. Upar menu mein **Actions** tab par click karein.
3. Sabse upar wale **"Build & Release Android APK"** workflow run par click karein.
4. Niche **Artifacts** section mein **`Aravalli-Furniture-APK`** file par click karke download karein.
5. Download hui zip file ko open karke **`AravalliFurniture-debug.apk`** ko apne Android phone mein install karein!
   *(Agar "Install unknown apps" permission maange to Settings mein allow karein).*

---

### तरीका 2: Android Studio se Run aur Install karna (PC / Laptop)

1. **Clone this repository:**
   ```bash
   git clone <YOUR_GITHUB_REPO_URL>
   cd aravalli-furniture
   ```
2. **Open in Android Studio:**
   - Android Studio open karein -> **File > Open** par click karein aur project folder select karein.
   - Gradle sync complete hone dein.
3. **Run on Device / Emulator:**
   - Apne Android phone ko USB cable se connect karein (USB Debugging ON karein) ya Android Emulator start karein.
   - Android Studio mein green **Run (▶)** button click karein.
   - App automatically phone mein install aur launch ho jayegi.

---

### तरीका 3: Terminal / Command Line se APK Build & Install karna

#### 1. APK Build karein:
```bash
# Linux / macOS:
./gradlew assembleDebug

# Windows (CMD / PowerShell):
gradlew.bat assembleDebug
```

#### 2. Built APK file location:
```
app/build/outputs/apk/debug/app-debug.apk
```

#### 3. ADB se Phone mein Install karein:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🌟 App Features (मुख्य फीचर्स)

- **Estimate & Quotation Generator**: Furniture items, wood selection (Sagwan Teak, Sheesham, Marine Ply, Real Plast WPC), dimensions, labor, GST (18%), discount, and instant WhatsApp quote sharing.
- **Confirm Orders Tracker**: Real-time production pipeline tracking (Confirmed → Cutting → Polish → Upholstery → Ready for Dispatch → Delivered) along with Advance and Balance payment ledger.
- **My Carpenter Loyalty Club**: Carpenter partner directory, loyalty points credit, coupon barcode scanner, and cash rewards.
- **Showroom Visitors & Leads**: Walk-in visitor log, furniture requirements, budget range, and follow-up reminder pipeline.
- **Festive Greeting Cards**: Customizable customer greeting cards (Diwali, Griha Pravesh, Vishwakarma Day, New Year) with Aravalli Furniture branding for WhatsApp sharing.
- **Estimate vs Order Analytics**: Dual-bar monthly sales graph with touch-to-inspect tooltips and conversion rate analysis.
- **Showroom Staff Check-In**: Attendance punch-in/out with location and timestamp logs.
- **Catalog & QR Scanner**: Showroom furniture catalog and interactive QR/barcode verification.

---

## 🛠️ Tech Stack & Requirements

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM + Clean Architecture
- **State Management:** StateFlow & Kotlin Coroutines
- **JDK:** Java 17 or Java 21
- **Minimum Android SDK:** Android 7.0 (API Level 24)
- **Target Android SDK:** Android 15 / 16 (API Level 36)

---

## 📄 License & Ownership
Copyright © 2026 Aravalli Furniture & Interiors. All rights reserved.
