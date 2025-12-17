<div align="center">

# 🎬 CinePlex - Movie Reservation System

**A modern, secure, and scalable RESTful API for theater seats reservations**

</div>

## Overview

## 🏗️ Architecture

### Technology stack

- Backend: Java 17, Spring Boot 3.5.3, Spring Data JPA
- Database: MySQL 8.0, Flyway, Redis
- Security: Spring Security, JWT 0.12.6
- API Documentation: Swagger OpenAPI
- Payment Processing: VNPay
- PDF Generator: Jasper Reports
- Barcode Generator: ZXing
- Email Service: Spring Mail
- Utilities: Lombok
- Upload Image: Cloudinary
- Ratelimiter: Resilience4j Ratelimiter
- Message Queue: RabbitMQ

## 🛠 Installation

### Requirements

- [Docker Desktop](https://docs.docker.com/desktop/setup/install/windows-install) in Window or [Docker](https://docs.docker.com/desktop/setup/install/linux/ubuntu) in Linux if setup with Docker

### Quick start guide

1. **Get the source code**

```bash
git clone https://github.com/DawnBreaker207/CinePlex
cd CinePlex
```

2. **Configure System**

For more information about:

- VNPay config: Check out [here](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html). If you don't have account, register [here](https://sandbox.vnpayment.vn/devreg)
- Test mail config: Check out [here](https://mailtrap.io/)

Create an `.env` file based on the `.example.env` with the following:

```bash
# Copy the example file
cp .example.env .env

#   Edit the .env file with you actual value
###   MySQL config
MYSQL_PORT=3307
MYSQL_DATABASE=db-name
MYSQL_ROOT_PASSWORD=mysql
###   Redis Config
REDIS_PORT=6379
###   Spring Port Config
SPRING_PORT=8888
###   Spring DB Config
SPRING_DATASOURCE_URL=connection-url
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=mysql
###   VNPay Config
VNPAY_URL=vnpay-url
VNPAY_TMN_CODE=vnpay-tmn-code
VNPAY_SECRET_KEY=vnpay-secret-key
VNPAY_RETURN_URL=vnpay-return-url
VNPAY_VERSION=vnpay-version
VNPAY_COMMAND=vnpay-command
VNPAY_ORDER_TYPE=vnpay-order-type
###   Default admin account - You can change default account
ADMIN_USERNAME=admin
ADMIN_EMAIL=admin@gmail.com
ADMIN_PASSWORD=admin
###   Default user account - You can change default account
USER_USERNAME=user
USER_EMAIL=user@gmail.com
USER_PASSWORD=user
###   Mailtrap Config
MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=
###   Refresh Token Config
JWT_COOKIE_NAME=
JWT_COOKIE_REFRESH_NAME=
JWT_SECRET=
JWT_EXPIRATION=
JWT_REFRESH_EXPIRATION=
### Config Cloudinary
FOLDER_NAME=
CLOUD_NAME=
API_KEY=
API_SECRET=
```

The connection URL format:

```bash
jdbc:mysql://mysql:3306/db
```

3. **Start the application with docker 🐳**

```bash
docker-compose up --build
```

or

```bash
docker-compose up --b
```

To end the system, run

```bash
docker-compose down -v
```

4. **Access the application**

- API: [http://localhost:8888](http://localhost:8888)
- Swagger UI: [http://localhost:8888/api/v1/swagger-ui.html](http://localhost:8888/api/v1/swagger-ui.html)

### Default Credentials

The system automatically creates two users on startup:

| Role  | Username | Password |
| ----- | -------- | -------- |
| Admin | `admin`  | `admin`  |
| User  | `user`   | `user`   |

## 🔥 API Reference

<p align="center">
  <a href="">
    <img src="https://img.shields.io/badge/Postman-View%20Complete%20Collection-4a4e69?style=for-the-badge&logo=postman&logoColor=white" alt="View Complete Collection">
  </a>
</p>

### API Categories

<p align="center">
  <a href="#-authentication">
    <img src="https://img.shields.io/badge/🔑_Authentication-4_Endpoints-4a4e69?style=flat-square" alt="Authentication: 4 Endpoints">
  </a>
  <a href="#-movies">
    <img src="https://img.shields.io/badge/🎬_Movies-6_Endpoints-4a4e69?style=flat-square" alt="Movies: 6 Endpoints">
  </a>
   <a href="#-users">
    <img src="https://img.shields.io/badge/🧑_Users-6_Endpoints-4a4e69?style=flat-square" alt="Users: 6 Endpoints">
  </a>
</p>
<p align="center">
  <a href="#-theaters">
    <img src="https://img.shields.io/badge/🏛️_Theaters-6_Endpoints-4a4e69?style=flat-square" alt="Theaters: 6 Endpoints">
  </a>
  <a href="#-showtimes">
    <img src="https://img.shields.io/badge/📅_Showtimes-8_Endpoints-4a4e69?style=flat-square" alt="Showtimes: 8 Endpoints">
  </a>
  <a href="#-seats">
    <img src="https://img.shields.io/badge/💺_Seats-3_Endpoints-4a4e69?style=flat-square" alt="Seats: 3 Endpoints">
  </a>
</p>
<p align="center">
  <a href="#-payment">
    <img src="https://img.shields.io/badge/💳_Payments-3_Endpoints-4a4e69?style=flat-square" alt="Payments: 3 Endpoints">
  </a>
  <a href="#-reservations">
    <img src="https://img.shields.io/badge/🎟️_Reservations-8_Endpoints-4a4e69?style=flat-square" alt="Reservations: 8 Endpoints">
  </a>
</p>
<p align="center">
<a href="#-report">
    <img src="https://img.shields.io/badge/📈_Report-1_Endpoint-4a4e69?style=flat-square" alt="Report: 1 Endpoint">
  </a>
  <a href="#-upload">
    <img src="https://img.shields.io/badge/📤_Upload-1_Endpoint-4a4e69?style=flat-square" alt="Upload: 1 Endpoint">
  </a>
   <a href="#-articles">
    <img src="https://img.shields.io/badge/📝_Articles-5_Endpoint-4a4e69?style=flat-square" alt="Articles: 5 Endpoint">
  </a>
  <a href="#-dashboard">
    <img src="https://img.shields.io/badge/📊_Dashboard-5_Endpoints-4a4e69?style=flat-square" alt="Dashboard: 5 Endpoints">
  </a>
</p>

### 🔑 Authentication

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/auth/register</code></td>
    <td>Register new user</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/auth/login</code></td>
    <td>Get JWT token</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/auth/logout</code></td>
    <td>Clear JWT token</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/auth/refresh-token</code></td>
    <td>Get JWT access token</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
</tbody>
</table>

### 🎬 Movies

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/movie</code></td>
    <td>Get all movies</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/movie/{id}</code></td>
    <td>Get movie by ID</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
   <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/movie/filmId/{id}</code></td>
    <td>Get movie by movie ID</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/movie</code></td>
    <td>Add movie</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/movie/{id}</code></td>
    <td>Update movie</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>DELETE</code></td>
    <td><code>/api/v1/movie/{id}</code></td>
    <td>Delete movie</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 🏛️ Theaters

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/theater</code></td>
    <td>Get all theaters</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/theater/$id}</code></td>
    <td>Get theater by ID</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/theater/search?location={location}</code></td>
    <td>Search theaters</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/theater</code></td>
    <td>Add theater</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/theater/{id}</code></td>
    <td>Update theater</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>DELETE</code></td>
    <td><code>/api/v1/theater/{id}</code></td>
    <td>Delete theater</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 📅 Showtimes

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/showtime?date={date}</code></td>
    <td>Get showtime by date</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/showtime/movies/{movieId}</code></td>
    <td>Get showtime by movie</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/showtime/theaters/{theaterId}</code></td>
    <td>Get showtime by theater</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/showtime/available</code></td>
    <td>Get available showtime</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/showtime/available/movies/{movieId}</code></td>
    <td>Get available showtime by movie</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/showtime</code></td>
    <td>Add showtime</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/showtime/{id}</code></td>
    <td>Update showtime</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>DELETE</code></td>
    <td><code>/api/v1/showtime/{id}</code></td>
    <td>Delete showtime</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 💺 Seats

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/seats/showtimes/{showtimeId}</code></td>
    <td>Get all seats</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/seats/showtimes/{showtimeId}/available</code></td>
    <td>Get available seats</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/seats/reservation/{reservation}</code></td>
    <td>Get all seat by reservation ID</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
</tbody>
</table>

### 📝 Article

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/article</code></td>
    <td>Get all article</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/article/{id}</code></td>
    <td>Get article by ID</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/article</code></td>
    <td>Add article</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/article/{id}</code></td>
    <td>Update article</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>DELETE</code></td>
    <td><code>/api/v1/article/{id}</code></td>
    <td>Delete article</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 💳 Payment

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/payment/create</code></td>
    <td>Create payment 3rd API URL</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
    <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/payment/update</code></td>
    <td>Update payment</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
</tbody>
</table>

### 🎟️ Reservations

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/reservation</code></td>
    <td>Get all reservation</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/reservation/{id}</code></td>
    <td>Get reservation by ID</td>
    <td>
      <img src="https://img.shields.io/badge/Owner-495057?style=flat-square" alt="Owner"> 
      <img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin">
    </td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/reservation/me</code></td>
    <td>Get own reservation by ID</td>
    <td>
      <img src="https://img.shields.io/badge/Owner-495057?style=flat-square" alt="Owner"> 
      <img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin">
    </td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/reservation/{id}/restore</code></td>
    <td>Get restore reservation</td>
    <td>
      <img src="https://img.shields.io/badge/Owner-495057?style=flat-square" alt="Owner"> 
      <img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin">
    </td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/reservation/init</code></td>
    <td>Init a new reservation</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/reservation/seatHold</code></td>
    <td>Choose and booking seat</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/reservation/confirm</code></td>
    <td>Create reservation</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>POST</code></td>
    <td><code>/api/v1/reservation/{reservationId}/cancel</code></td>
    <td>Cancel reservation</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
</tbody>
</table>

### 📊 Dashboard

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/dashboard/metrics</code></td>
    <td>Get dashboard metrics</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/dashboard/revenue</code></td>
    <td>Get dashboard revenue</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
   <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/dashboard/top-movies</code></td>
    <td>Get top 5 movie</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/dashboard/top-theaters</code></td>
    <td>Get top 5 theater</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
    <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/dashboard/payment-distribution</code></td>
    <td>Get payment distribution</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 📈 Report

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/report/export/{format}</code></td>
    <td>Export report</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
</tbody>
</table>

### 📤 Upload

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/cloudinary/upload</code></td>
    <td>Upload to cloudinary</td>
    <td><img src="https://img.shields.io/badge/Public-6c757d?style=flat-square" alt="Public"></td>
  </tr>
</tbody>
</table>

### 🧑 Users

<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
    <th>Access</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/user</code></td>
    <td>Get all user</td>
    <td><img src="https://img.shields.io/badge/Admin-343a40?style=flat-square" alt="Admin"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/role/{roleName}</code></td>
    <td>Get user role</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/user/{id}</code></td>
    <td>Get user by ID</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>GET</code></td>
    <td><code>/api/v1/user/email/{email}</code></td>
    <td>Get user by email</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/user/update/{id}/status</code></td>
    <td>Update user by status</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
  <tr>
    <td><code>PUT</code></td>
    <td><code>/api/v1/user/update/{id}/profile</code></td>
    <td>Update user by id</td>
    <td><img src="https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square" alt="Authenticated"></td>
  </tr>
</tbody>
</table>

## 📖 Documentation

### Response format

All API responses follow a consistent format:

- Normal

```json
  {
    "code": 200,
    "message": "Success",
    "data": {"Response data here"}
  }
```

- With Page

```json
  {
    "code": 200,
    "message": "Success",
    "data": {
      content: ["Response data here"],
      pagination: {
        pageNumber: 0,
        pageSize: 20,
        totalElements: 10,
        totalPages: 1
      }
    }
  }
```
