# Game Backlog Application

A full-stack mobile solution developed for the **WGU D424 Software Engineering Capstone**. This application provides a modern, high-contrast dashboard for gamers to manage their library progress across multiple platforms and storefronts.

## 🚀 Live Deployment
The application is hosted via GitHub Pages. You can visit the landing page to view project details and download the Android installation file (APK).

**[View Hosted Landing Page & Download APK](https://brunettisergio.github.io/game-backlog-app/)**

---

## ✨ Key Features
*   **Modern Dark UI:** A high-contrast, OLED-optimized theme designed for readability and professional aesthetics.
*   **Expandable Game Cards:** Interactive list items that reveal detailed metadata, inline editing tools, and status controls when tapped.
*   **Multi-Platform Support:** Robust platform management allowing users to tag games across Steam, Epic Games, GOG, Amazon, PSN, and XBOX using a custom multi-select interface.
*   **Advanced Analytics:** A summary report dashboard featuring automated **Completion Rate** calculations and detailed library timestamps.
*   **Real-Time Search:** Instant, case-insensitive filtering of the entire game collection.
*   **Optimistic UI Sync:** High-performance data handling that updates the UI instantly while synchronizing with the cloud in the background.

---

## 🛠️ Tech Stack
*   **Frontend:** Android (Java) with Room Persistence Library for local caching.
*   **Backend:** Spring Boot (Java) REST API.
*   **Database:** PostgreSQL (Cloud/Remote) and SQLite (Local).
*   **Networking:** Retrofit 2 for type-safe HTTP communication.
*   **Architecture:** MVVM (Model-View-ViewModel) pattern.

---

## 📁 Project Structure
*   `/android-app`: The source code for the Android mobile client.
*   `/backend-api`: The Spring Boot source code and database configurations.
*   `/documentation`: Contains the official Design Document, Maintenance Guide, User Guide, and Testing Reports.
*   `index.html`: The landing page used for GitHub Pages hosting.

---

## 🔧 Setup & Installation

### For Users
1.  Download the `app-release.apk` from the hosted link above.
2.  Enable "Install from Unknown Sources" in your Android settings.
3.  Install and launch the **Game Backlog** app.

### For Developers
For detailed instructions on setting up the JDK, configuring the PostgreSQL database, and running the development environment, please refer to the **[Maintenance & Setup Guide](documentation/Maintenance_and_Setup_Guide.docx)**.

---

## 📜 Academic Integrity
This project was developed as a requirement for the WGU D424 Software Engineering Capstone. All architectural decisions, documentation, and source code modifications were performed to satisfy the specific grading criteria of the course.
