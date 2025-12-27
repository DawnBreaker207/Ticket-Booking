# <span id="setup-guide"></span> ⚙️ Operations & Setup Guide

This guide provides step-by-step instructions to set up the CinePlex development environment and deploy the system using Docker.

### 🛠 Prerequisites

- **Docker:** Version 24.0.0+
- **Docker Compose:** Version 2.20.0+
- **Memory:** Minimum 4GB RAM allocated to Docker.

### ⚡ Quick start guide

1. **Clone the repository**

```bash
git clone https://github.com/DawnBreaker207/CinePlex
cd CinePlex
```

2. **Configure Environment Variables**

The system uses an .env file to manage secrets and infrastructure settings. Create this file from the provided template:
Create an `.env` file based on the `.example.env` with the following:

```bash
# Copy the example file
cp .example.env .env
```

Open the .env file and update the values as categorized below:

> [!IMPORTANT]
> When running inside Docker, the SPRING_DATASOURCE_URL must use the service name mysql instead of localhost.

    Correct Format: jdbc:mysql://mysql:3306/cineplex_db

🔑 Environment Variable Reference

| Category       | Key                          | Default / Description                 |
| -------------- | ---------------------------- | ------------------------------------- |
| Infrastructure | `MYSQL_PORT`                 | `3307` (External Port)                |
|                | `REDIS_PORT`                 | `6379`                                |
|                | `SPRING_PORT`                | `8888`                                |
| Database       | `SPRING_DATASOURCE_URL`      | `jdbc:mysql://mysql:3306/cineplex_db` |
|                | `SPRING_DATASOURCE_USERNAME` | root                                  |
| VNPay          | `VNPAY_TMN_CODE`             | Merchant Code from Sandbox            |
|                | `VNPAY_SECRET_KEY`           | Hash Secret from Sandbox              |
|                | `VNPAY_RETURN_URL`           | `http://localhost:4200/paymentResult` |
| Mailtrap       | `MAIL_HOST`                  | `sandbox.smtp.mailtrap.io`            |
| Auth (JWT)     | `JWT_SECRET`                 | Your secure random secret key         |
|                | `JWT_EXPIRATION`             | e.g., 86400000 (1 day)                |
| Cloudinary     | `CLOUD_NAME`                 | Your Cloudinary Cloud Name            |
|                | `API_KEY` / `API_SECRET`     | Your API Credentials                  |

<details>
  <summary><b>📄 Click to view Full .env Template</b></summary>

```bash
#   Edit the .env file with you actual value
###   MySQL config
MYSQL_PORT=3307
MYSQL_DATABASE=cineplex_db
MYSQL_ROOT_PASSWORD=mysql

###   Redis Config
REDIS_PORT=6379

###   Spring Port Config
SPRING_PORT=8888

###   Spring DB Config
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/cineplex_db
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

</details>

3. **Deploy with Docker 🐳**

```bash
# Build and start all services
docker compose up --build
```

To stop the system and remove all associated volumes:

```bash
docker compose down -v
```

## 🌐 External Services Configuration

1. VNPay Sandbox (Payment Gateway)

- Documentation: [Here](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html)
- Registration: [Here](https://sandbox.vnpayment.vn/devreg)

2. MailTrap (Email Testing)

- Sign up at [Mailtrap.io](https://mailtrap.io/)
- Use the provided SMTP credentials to fill in the `MAIL` variables in your .env. This allows the system to send asynchronous booking confirmations and reports without a real mail server.

3. Cloudinary (Media CDN)

- Images for movie posters and articles are stored here.
- Make sure to create the folder specified in FOLDER_NAME in your Cloudinary Media Library.

## 👥 Default Credentials

Once the system is running, you can access the applications at the following endpoints:

| Application  | URL                                            |
| ------------ | ---------------------------------------------- |
| Frontend App | `http://localhost:4200`                        |
| Backend API  | `http://localhost:8888`                        |
| Swagger UI   | `http://localhost:8888/api/v1/swagger-ui.html` |

<details>
<summary>
🔑 <b>Default Credentials (Click to expand)</b>
</summary>

| Role  | Username | Email             | Password |
| ----- | -------- | ----------------- | -------- |
| Admin | `admin`  | `admin@gmail.com` | `admin`  |
| User  | `user`   | `user@gmail.com`  | `user`   |

</details>
