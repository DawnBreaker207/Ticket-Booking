# [Ticket-Booking](https://github.com/DawnBreaker207/Ticket-Booking)

A booking film tickets system

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

## 🛠 Installation

### Requirements

- [Docker Desktop](https://docs.docker.com/desktop/setup/install/windows-install) in Window or [Docker](https://docs.docker.com/desktop/setup/install/linux/ubuntu) in Linux if setup with Docker

### Get the source code

```bash
git clone https://github.com/DawnBreaker207/Ticket-Booking
cd Ticket-Booking
```

### Configure System

For more information about VNPay config check out [here](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html)

If you don't have account, register [here](https://sandbox.vnpayment.vn/devreg)

Create an <code>.env</code> file with the following:

```bash
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
```

The connection URL format:

```bash
jdbc:mysql://mysql:3306/db
```

To access the application:

- Swagger UI: `http://localhost:8888/api/v1/swagger-ui.html`

### Default Credentials

The system automatically creates two users on startup:

| Role  | Username | Password |
| ----- | -------- | -------- |
| Admin | `admin`  | `admin`  |
| User  | `user`   | `user`   |

### 🐳 Start with Docker

To build the backend system, run:

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

## 🔄 Getting Updates

To get the latest features, simply do a pull, install any new dependencies:

```git
git pull
```

and rebuild

```bash
docker-compose up --b
```
