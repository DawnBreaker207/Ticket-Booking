<div align="center">

# 🎬 CinePlex - Reservation System

**A high-concurrency, modular monolith platform for theater seat reservations.**

[![Backend](https://img.shields.io/badge/Backend-%20Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot)](./docs/architecture/overview.md)
[![Frontend](https://img.shields.io/badge/Frontend-Angular%20-DD0031?style=for-the-badge&logo=angular)](./docs/architecture/overview.md)
[![Infrastructure](https://img.shields.io/badge/Infrastructure-Docker-2496ED?style=for-the-badge&logo=docker)](./docs/deployment/setup.md)

</div>

## 📝 Project Overview

**CinePlex** is built to solve the messiest part of cinema management: **Double Booking**. By combining **Distributed Locking** via Redis and a **Modular Monolith** architecture, the system handles high-traffic seat selection without crashing the database or allowing race conditions.

It serves as a technical showcase for how to keep a large system clean using isolated domain modules and a reactive frontend.

## 📁 Project structure

```text
├── frontend/             # Angular Application
│   ├── src/app/
│   │   ├── core/         # Singleton services, interceptors, and guards
│   │   ├── domain/       # Models, domain services and state management
│   │   ├── features/     # Feature modules (Admin dashboard, Client portal, Error pages)
│   │   ├── layout/       # Static layout components (Header, Footer, Sidebar)
│   │   └── shared/       # Reusable components, pipes, directives, and utility services
├── backend/              # Spring Boot Modular Monolith
│   ├── dawn-web/         # Application entry point & API Gateway Layer
│   ├── dawn-common/      # Shared kernel: Common utilities, base DTOs, and global exceptions
│   ├── dawn-booking/     # Domain: Seat reservation lifecycle & Redis locking logic
│   ├── dawn-cinema/      # Domain: Theater management and showtimes
│   ├── dawn-identity/    # Domain: Security core, JWT, and RBAC
│   └── dawn-*            # Other isolated domain modules (Catalog, Notification, etc.)
├── infra/                # Infrastructure & Orchestration
│   ├── docker-compose.yml# Multi-container setup (App, MySQL, Redis, RabbitMQ)
│   └── nginx.conf        # Reverse proxy, SSL termination, and Load balancing
└── docs/                 # Extended Technical Documentation & API Refs
```

## 🏗️ System Architecture

- **Backend (`dawn-*` modules):** A modular monolith approach where core contexts (Booking, Catalog, Identity) are isolated, sharing infrastructure through a dedicated `common` module.
- **Frontend (Angular):** A domain-centric frontend using **NgRx** for state management, ensuring a unidirectional and predictable data flow.
- **Infrastructure:** Real-time seat "holding" is handled by **Redis**, while **RabbitMQ** manages background jobs like email triggers and PDF generation.

> [!TIP] > **Deep Dive:** Explore our [System Overview Documentation](./docs/architecture/overview.md) for a full breakdown

---

## 🛠️ Technology stack

| Layer              | Technologies                                                                      |
| :----------------- | :-------------------------------------------------------------------------------- |
| **Backend**        | Java 17, Spring Boot 3.5, Spring Data JPA, Spring Security (JWT), Flyway          |
| **Frontend**       | Angular 17+, NgRx (Store & Effects), RxJS, TailwindCSS, Ng-Zorro                  |
| **Infrastructure** | MySQL 8.0, Redis (Pub/Sub & KV), RabbitMQ (Message Queue), Docker                 |
| **Services**       | VNPay(Payment), Jasper Reports(PDF), ZXing (Barcode), Cloudinary (Images Storage) |

---

## 🔥 Features

- **Distributed Seat Locking:** Uses Redis TTL to lock seats for 15 minutes during checkout.
- **Real-time Sync:** Redis Pub/Sub pushes seat status updates to all active users instantly.
- **Async Processing:** RabbitMQ offloads heavy JasperReport generation and emailing to background workers.
- **Secure Checkout:** Full integration with the VNPay Sandbox gateway including checksum verification.
- **Admin Analytics:** Real-time dashboard for revenue, occupancy rates, and movie performance.

---

## 🚀 Quick start (Dockerized 🐳)

```bash
# 1. Clone & Setup Environment
git clone https://github.com/DawnBreaker207/CinePlex

cd CinePlex

cp .example.env .env # ⚠️ See /docs/deployment/setup.md for full .env guide

# 2. Run the Cluster
docker compose up --build
```

> 🔑 **Default Credentials:** See the [Credentials Section](./docs/deployment/setup.md#credentials) in our Setup Guide.

## 🔥 API Reference

CinePlex exposes a RESTful API. For a hands-on experience, you can import our Postman collection.

> 💡 **Detailed Specification:** For full request/response models and error codes, please visit our **[Full API Documentation](./docs/api/endpoints.md)** or check the **[Swagger UI](http://localhost:8888/api/v1/swagger-ui.html)**.

### 📑 API Categories

| Category   | Description                            | Reference                                      |
| :--------- | :------------------------------------- | :--------------------------------------------- |
| Identity   | Auth,JWT Rotation, User & Role Manager | [Link](./docs/api/endpoints.md#authentication) |
| Catalog    | Movies, Articles                       | [Link](./docs/api/endpoints.md#movies)         |
| Cinema     | Theaters, Showtimes                    | [Link](./docs/api/endpoints.md#theaters)       |
| Booking    | Real-time Seat Locking & Reservations  | [Link](./docs/api/endpoints.md#reservations)   |
| Management | Analytics, Jasper Reports, Dashboard   | [Link](./docs/api/endpoints.md#dashboard)      |

<p align="left">
  <a href="./docs/postman/Collection.json" target="_blank">
    <img src="https://img.shields.io/badge/Postman-Download_Collection-FF6C37?style=flat-square&logo=postman&logoColor=white" alt="Download Postman Collection">
  </a>
</p>

---

📚 Documentation Index

| Section                                                | Content                                                    |
| :----------------------------------------------------- | :--------------------------------------------------------- |
| [Business Logic](./docs/business/user-story.md)        | User Stories, Business Rules, and Concurrency logic        |
| [System Architecture](./docs/architecture/overview.md) | Backend Multiple Module and Frontend structure             |
| [Deployment Guide](./docs/deployment/setup.md)         | Full `.env` configuration, VNPay Setup, and Mailtrap guide |
