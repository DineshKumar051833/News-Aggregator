# 📰 News Aggregator Web Application

A full-stack **News Aggregator application** built using **Spring Boot (Java)** for the backend and **JavaScript (Fetch API) with HTML/CSS** for the frontend.  
The application fetches real-time news from an external News API, supports user authentication, and allows users to save and manage news articles using **session-based login**.

---

## 🚀 Features

- 🌐 Fetch real-time news from an external News API
- 👤 User Sign Up & Login
- 🧠 Session-based authentication using HttpSession
- 💾 Save favorite news articles
- 🗑️ Delete saved news
- ⚡ Frontend communication using JavaScript Fetch API
- 📡 REST + MVC hybrid architecture

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- HTML
- CSS (Bootstrap)
- JavaScript (Fetch API)

### Tools & APIs
- External News API
- Postman
  
---

## 🌐 Application Flow

1. User lands on `index.html`
2. Public news is fetched using JavaScript from backend APIs
3. User signs up or logs in
4. After login, user is redirected to `Home.html`
5. User can search news and save articles
6. Saved articles are viewed and managed in `Profile.html`
7. User session is maintained using `HttpSession`

---

## 📡 Backend API Endpoints

### 📰 News APIs

| Method | Endpoint | Description |
|------|---------|-------------|
| GET | `/api/news` | Fetch top headlines |
| GET | `/home` | Fetch news for logged-in user |
| POST | `/saved` | Save a news article |
| GET | `/profile` | Get saved news |
| DELETE | `/profile/delete/{newsId}` | Delete saved news |

---

### 👤 User APIs

| Method | Endpoint | Description |
|------|---------|-------------|
| GET | `/` | Public Home page |
| GET | `/login` | Login page |
| GET | `/signup` | Sign up page |
| POST | `/signupUser` | Register user |
| POST | `/loginUser` | Authenticate user |
| GET | `/logout` | Logout user |

