# 🎟️ Ticket Sales & Reservation System (OOP Edition)

A modular, multi-threaded Java desktop application built using **Object-Oriented Programming (OOP)** principles and **Client-Server Architecture** over TCP Sockets. The system supports real-time ticket booking, cancellation, booking details inspection, file persistence, and a built-in verification runner.

---

## 🚀 Key Features

- **OOP Architecture:** Structured into clear packages (`model`, `exception`, `service`, `network`, `ui`, `test`) applying Abstraction, Inheritance, Polymorphism, Composition, and Custom Exceptions.
- **Multiple Ticket Types:** Supports `CINEMA`, `MATCH`, and `VIP` tickets with polymorphic final price calculation (`calculateFinalPrice()`).
- **25 Pre-loaded Tickets:** Manages tickets `101` through `125` across cinema screenings, sports matches, and VIP events.
- **Customer & Booking Tracking:** Captures customer name and phone number upon booking, displays full booking details in a centered popup dialog, and logs bookings to `bookings_history.log`.
- **Client-Server over TCP Sockets:** Multi-threaded server (`Server` + `ClientHandler`) handling concurrent client requests safely.
- **Data Persistence:** Persists ticket availability and customer booking info in `tickets.txt`.
- **Built-in System Verification:** Includes `SystemVerification.java` to verify pricing polymorphism, booking/cancellation flows, and duplicate-booking exception handling without external test libraries.

---

## 🛠️ Tech Stack & OOP Concepts

- **Language:** Java
- **OOP Principles:** `abstract` classes, `interface`, Inheritance, Method Overriding (Polymorphism), Composition (`Booking` with `Customer` and `Ticket`), Custom Exceptions (`BookingException`)
- **GUI:** Java Swing (`JFrame`, `JTable`, `JOptionPane`)
- **Networking & Concurrency:** Java Sockets (`ServerSocket`, `Socket`), Multi-threading (`Thread`), `synchronized` service methods
- **Persistence & Logging:** File I/O (`BufferedReader`, `PrintWriter`)

---

## 📂 Project Structure

```text
src/
├── model/
│   ├── Ticket.java
│   ├── CinemaTicket.java
│   ├── MatchTicket.java
│   ├── VIPTicket.java
│   ├── Customer.java
│   └── Booking.java
├── exception/
│   └── BookingException.java
├── service/
│   ├── StorageService.java
│   ├── FileStorageManager.java
│   └── TicketService.java
├── network/
│   ├── Server.java
│   └── ClientHandler.java
├── ui/
│   └── ClientGUI.java
└── test/
    └── SystemVerification.java

▶️ How to Run

Run network.Server to start the TCP server on port 5000.

Run ui.ClientGUI to launch the desktop client interface.

Run test.SystemVerification to execute the built-in OOP verification checks.

👨‍💻 Author
Belal Elweseya — OOP Course Project