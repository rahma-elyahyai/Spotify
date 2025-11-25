# 🎵 Music Streaming System (Melodify)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![SQL Server](https://img.shields.io/badge/MS%20SQL%20Server-CC2927?style=for-the-badge&logo=microsoft-sql-server&logoColor=white)

A **full-stack music streaming platform** inspired by Spotify, built using **Spring Boot**, **React (Vite)**, and **MS SQL Server**.  
This application allows users to stream songs, create playlists, follow artists, and view analytics — with **secure role-based access** for Users, Artists, and Admins.

---

## 🚀 Features

### 🎧 Music Playback
- Custom HTML5 audio player with queue management, shuffle, seek, and volume controls.
- Persistent listening history and real-time play count tracking.

### 🔒 Role-Based Access
- **User:** Discover, play, and manage playlists.  
- **Artist:** Upload and manage songs/albums, schedule concerts, and view analytics.  
- **Admin:** Approve/reject songs, monitor user activity, and access moderation dashboards.

### 🧩 Artist Workflows
- Create, edit, and manage albums and tracks.
- Schedule and manage concerts.
- Track play counts and user interactions.

### 📊 Admin Dashboard
- Moderation panel for song verification and content approval.
- Analytics dashboards (ApexCharts) for top songs, listening duration, and genre trends.

### 🎼 Playlist Management
- Create, update, and share playlists using **AES encryption (CryptoJS)**.
- Secure, read-only shared playlist views via generated URLs.

### ⚙️ Performance & Observability
- Implemented **Spring AOP** logging for method-level performance monitoring.
- Optimized API calls, debounced search inputs, and caching for smooth UX.

### 🧠 Interactive Features
- Voice command toggle using **React Speech Recognition**.
- Toast notifications, skeleton loaders, and smooth UI transitions with **Framer Motion**.

---

## 🛠️ Tech Stack

| Layer | Technologies |
|-------|---------------|
| **Frontend** | React (Vite), Material UI, Bootstrap, HTML5 Audio API, ApexCharts, Axios |
| **Backend** | Spring Boot, Spring Security, Spring AOP, RESTful APIs |
| **Database** | Microsoft SQL Server |
| **Libraries & Tools** | JSZip, FileSaver, CryptoJS, html2canvas, React Router, Day.js |

---

## 🧰 System Architecture
Frontend (React)
│
├── REST APIs (Spring Boot)
│ ├── Authentication & Role Management
│ ├── Songs, Playlists, Artists
│ ├── Analytics & Moderation
│
└── Database (MS SQL Server)
├── Users / Artists / Admins
├── Songs / Albums / Playlists
├── Listening History / Notifications

👤 Author

Chandu B R
Junior Machine Learning Engineer | Full Stack Developer
📍 Bangalore, India

