# ⏱ FCT — Feuille de Contrôle de Temps

FCT (Feuille de Contrôle de Temps) is a web application that allows employees to track and report their monthly work
progress by assigning completion percentages to their professional activities such as projects, presentations, meetings,
and other tasks.

At the end of each month, each employee fills in the progress of their activities, and the administration can monitor
productivity and performance through structured reports.

---

## Architecture

This project follows a **classic MVC architecture**:

- **Model** → JPA Entities (User, Activity, periode, etc.)
- **View** → Angular Frontend
- **Controller** → Spring REST Controllers

The application is:
- **Backend**: Spring Boot (running on localhost)
- **Frontend**: Angular
- **Database**: PostgreSQL (Dockerized)

---

##  Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring MVC
- Spring Security + JWT
- Spring Data JPA
- Swagger (OpenAPI)
- Maven

### Frontend
- Angular

### Database
- PostgreSQL (Docker)

### DevOps
- Docker
- Docker Compose

---

## Main Functionalities

- Authentication using JWT
- Employee management
- Activity management (Projects, Presentations, etc.)
- Monthly work report submission
- Secure API access with Spring Security
- API documentation using Swagger

---

## Class Diagram (UML)

The Class Diagram explains the core business logic and relationships between entities.
![class Diagram](docs/FCT_class_diagram.PNG)

## API Documentation (Swagger)
http://localhost:8080/swagger-ui.html

## Docker Setup (PostgreSQL Only)
- start database: docker-compose up -d
- stop database: docker-compose down

## Installation & Run
- 1- clone the repository: 
- git clone : https://github.com/SoumayaTemmar/FCT_APP_backend.git
- cd FCT_APP_backend

- 2- Run Backend (Spring Boot)
- mvn clean install
- mvn spring-boot:run


👤 Author

Name: Soumaya Temmar

GitHub: https://github.com/SoumayaTemmar/

## ⚠️ License Notice

You are allowed to **view and learn from the source code**, but you are **NOT allowed to host,
sell, or use this application for commercial purposes** without explicit permission from the author.


## © Copyright

© 2025 Soumaya Temmar. All rights reserved.
