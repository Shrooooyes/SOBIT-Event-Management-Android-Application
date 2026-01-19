# 📱 SOBIT – Event Management Android Application

SOBIT is an Android-based Event Management application developed using **Java** and **XML** in **Android Studio**.  
The application helps event organizers efficiently manage events, clients, schedules, and payments using a centralized mobile platform with **Firebase Realtime Database**.

---

## 📌 Features

- 🔐 User Authentication (Firebase)
- 📊 Dashboard with event summary
- ➕ Add new events
- 📋 View all events using RecyclerView
- 📝 View detailed event information
- ✏️ Edit existing events
- ❌ Delete events
- 📲 WhatsApp integration for client communication
- ☁️ Real-time data storage using Firebase

---

## 🛠️ Technologies Used

### Frontend
- Java
- XML
- Material Design Components

### Backend
- Firebase Realtime Database
- Firebase Authentication

### Tools
- Android Studio
- Gradle
- Firebase Console

---

## 🏗️ System Architecture
```
User
↓
Android Application (Java + XML)
↓
Firebase Realtime Database
```
- The Android app acts as the client
- Firebase stores and syncs event data in real time
- Data is stored in JSON format

---

## 🔄 Application Flow
```
Login
↓
Dashboard
↓
Add Event / View Events
↓
Event Details
↓
Edit / Delete Event
```

---

## 🗄️ Database Structure (Firebase Realtime Database)
```
events
└── eventId
├── eventName
├── clientName
├── phone
├── date
├── time
├── venue
├── eventType
├── totalAmount
├── advancePaid
├── remainingAmount
├── status
```

---

## 📸 Screens Included in the App

- Login Screen
- Dashboard
- Add Event Screen
- View Events List
- Event Details Screen
- Edit Event Screen
- WhatsApp Client Chat

---

## ⚠️ Limitations

- Push notifications using Firebase Cloud Functions require a paid (Blaze) plan
- Online payment gateway not implemented
- Internet connection required for real-time database access

---

## 🚀 Future Enhancements

- Push notifications using Firebase Cloud Messaging
- Online payment integration
- Role-based multi-user system
- Event analytics and reports
- Uploading event reference images

---

## 📂 Project Setup

1. Clone the repository
2. Open the project in **Android Studio**
3. Connect the project to **Firebase**
4. Enable:
   - Firebase Authentication
   - Firebase Realtime Database
5. Sync Gradle and run the app

---

## 🎓 Academic Use

This project is developed as an **academic Android application project** and is suitable for:
- Mini Project
- Final Year Project
- Android Development Lab
- Firebase Integration Demonstration

---

## 📄 Documentation

Detailed project documentation is available in the attached PDF:
[📱 SOBIT – Event Management Android Application.pdf](https://github.com/user-attachments/files/24721676/SOBIT.Event.Management.Android.Application.pdf)


---

## 👨‍💻 Developer

**Project Name:** SOBIT – Event Management App  
**Platform:** Android  
**Language:** Java  

---

## 📜 License

This project is created for educational purposes only.
