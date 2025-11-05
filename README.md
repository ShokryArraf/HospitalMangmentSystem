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

TimeSlotNotAvailableException
DuplicateResourceException
ResourceNotFoundException
InvalidOperationException
GlobalExceptionHandler

🧭 API Endpoints
-You can check these endpoints using Postman.
👨‍⚕️ DoctorController

Base URL: /doctors

🔹 Basic Endpoints

POST /doctors — Create a new doctor

GET /doctors — Get all doctors

GET /doctors/{id} — Get a doctor by ID

PUT /doctors/{id} — Update a doctor

DELETE /doctors/{id} — Delete a doctor

🔥 Advanced Endpoints

GET /doctors/by-specialization-map — Get doctors grouped by specialization (Map format)

GET /doctors/top-rated?limit= — Get top-rated doctors

GET /doctors/experience-range?min=&max= — Get doctors by experience range

GET /doctors/average-rating-by-spec — Get average rating by specialization

GET /doctors/most-appointments?limit= — Get doctors with the most appointments

🧍 PatientController

Base URL: /patients

🔹 Basic Endpoints

POST /patients — Create a new patient

GET /patients — Get all patients

GET /patients/{id} — Get a patient by ID

PUT /patients/{id} — Update a patient

DELETE /patients/{id} — Delete a patient

🔥 Advanced Endpoints

GET /patients/age-range?min=&max= — Get patients within an age range

GET /patients/age-statistics — Get patient age statistics (min, max, average)

GET /patients/grouped-by-age — Get patients grouped by age category

GET /patients/most-appointments?limit= — Get patients with the most appointments

GET /patients/search?keyword=john — Search patients by name

📅 AppointmentController

Base URL: /appointments

🔹 Basic Endpoints

POST /appointments — Create a new appointment

GET /appointments — Get all appointments

GET /appointments/{id} — Get an appointment by ID

PUT /appointments/{id} — Update an appointment

DELETE /appointments/{id} — Delete an appointment

PUT /appointments/{id}/cancel — Cancel an appointment

PUT /appointments/{id}/complete — Mark an appointment as completed

🔥 Advanced Endpoints

GET /appointments/priority/{priority} — Get appointments by priority

GET /appointments/upcoming?days= — Get upcoming appointments within a number of days

GET /appointments/statistics-by-status — Get appointment statistics grouped by status

GET /appointments/date-range?start=--&end=-- — Get appointments within a specific date range

GET /appointments/daily-schedule/{doctorId}?date=-- — Get a doctor's daily schedule for a specific date

GET /appointments/available-slots/{doctorId}?date=-- — Get available time slots for a doctor

GET /appointments/most-busy-day — Get the most busy day based on appointments

GET /appointments/duration-range?min=&max= — Get appointments filtered by duration range

GET /appointments/cancelled-report — Get a report of cancelled appointments


✨ Future Improvements

🗄️ Replace in-memory storage with a real database (MySQL / PostgreSQL / H2)

🔐 Add authentication & authorization

🧪 Implement unit & integration tests

🌐 Expand frontend UI with filters and analytics


👨‍💻 Author
Shokry Arraf
📧 arraf.shokry.as@gmail.com
🔗 [Shokry Arraf - LinkedIn](https://www.linkedin.com/in/Shokry-Arraf/)

