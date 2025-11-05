# 🏥 Hospital Management System (Spring Boot)

## 📘 Overview
The **Hospital Management System** is a backend application built with **Spring Boot** that manages doctors, patients, and appointments.  
It provides full CRUD operations, filtering, analytics (e.g., top-rated doctors, age statistics), and in-memory data handling — perfect for learning, demos, or small-scale management.

---

## ⚙️ Technologies Used
- ☕ **Java 17+**
- 🌱 **Spring Boot**
- 🧩 **Spring Web**
- 🧠 **Lombok**
- ✅ **Jakarta Validation**
- 🏗️ **Maven**
- ⚡ **Custom Exception Handling**

---

## 🧩 Architecture
The project follows a **layered architecture**:

Controller → Service → DTO → In-memory Storage


Each entity (Doctor, Patient, Appointment) includes:
- `CreateDto` — for validated input requests  
- `Dto` — for returning responses  
- `Service` — for business logic and storage  

---

## 🧑‍⚕️ Doctor Management

### 🔹 Features
- Create, update, delete, and fetch doctors  
- Group doctors by specialization  
- Get **top-rated doctors** (with `limit`)  
- Filter by **experience range**  
- Calculate **average rating by specialization**  
- Find **doctors with most appointments**

🧍 Patient Management
🔹 Features

Create, update, delete, and fetch patients

Search by first or last name

View age statistics

Get patients with most appointments


📅 Appointment Management
📋 Fields (AppointmentCreateDto)
Field	Type	Validation
doctorId	Long	required
patientId	Long	required
appointmentDate	String	YYYY-MM-DD
appointmentTime	String	HH:MM (24-hour)
status	String	SCHEDULED, COMPLETED, CANCELLED
duration	Integer	15 / 30 / 45 / 60
priority	String	LOW, MEDIUM, HIGH, URGENT
notes	String	optional, max 500 chars


🚀 How to Run
1️⃣ Clone the Repository
git clone https://github.com/ShokryArraf/HospitalManagementSystem.git
cd HospitalManagementSystem

2️⃣ Build & Run
mvn spring-boot:run

3️⃣ Access
http://localhost:8080

🧠 Exception Handling

Custom exceptions ensure clean and meaningful error responses:

DuplicateResourceException

ResourceNotFoundException

InvalidOperationException
