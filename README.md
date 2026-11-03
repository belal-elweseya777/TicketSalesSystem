# 🎟️ Ticket Sales System (Client-Server Desktop Application)

A robust, multi-threaded Java desktop application built using **Client-Server Architecture** via TCP Sockets. The system provides a real-time ticket booking platform with data persistence, concurrent user handling, and an intuitive GUI.

---

## 🚀 Key Features

* **Client-Server Architecture:** Full TCP Socket connection handling real-time requests.
* **Multi-threaded Server:** Concurrent client connections handled safely via Java Threads.
* **Graphical User Interface (GUI):** Clean and interactive interface built using `javax.swing`.
* **Data Persistence:** Automatic synchronization of ticket status saved to `tickets.txt`.
* **20 Sequential Tickets:** Manages tickets range `101` to `120` dynamically.
* **Search & Notifications:** Instant search by Ticket ID with interactive visual status popups (`AVAILABLE` / `BOOKED`).
* **Unit Testing:** Verified core domain logic using JUnit (`TicketTest.java`).

---

## 🛠️ Tech Stack & Concepts

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing (`JFrame`, `JTextArea`, `JOptionPane`)
* **Networking:** Java Socket Programming (`ServerSocket`, `Socket`)
* **Concurrency:** Java Multi-threading (`Thread`)
* **Persistence:** File I/O (`BufferedReader`, `PrintWriter`)
* **Version Control:** Git & GitHub

---

## 📂 Project Structure

* `Client.java` — Swing GUI Client Application
* `Server.java` — Multi-threaded Socket Server
* `TicketManager.java` — Core business logic & persistence
* `Ticket.java` — Ticket Model (ID, Event, Price, Status)
* `TicketTest.java` — Unit testing suite
* `tickets.txt` — Auto-generated text file storage

---

## 👨‍💻 Author
**Belal Elweseya** - *OOP Course Project*
