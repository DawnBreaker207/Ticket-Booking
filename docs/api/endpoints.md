## 📖 API Specification

This document provides a detailed reference for all available endpoints in the CinePlex system.

## 🚀 Quick Navigation

- [🧪 Testing with Postman](#postman)
- [🔑 Authentication](#authentication)
- [🎬 Movies](#movies)
- [🏛️ Theaters](#theaters)
- [📅 Showtimes](#showtimes)
- [💺 Seats](#seats)
- [🎟️ Reservations](#reservation)
- [💳 Payments](#payment)
- [📊 Dashboard](#dashboard)
- [📈 Reports](#report)
- [🧑 Users](#users)
- [📤 Upload](#upload)
- [📝 Articles](#articles)

---

## <span id="postman"></span> 🧪 Testing with Postman

We provide a pre-configured collection to help you test the APIs instantly.

### 📥 Setup Instructions

1. **Download:** the [Collection](./../postman/Collection.json) and [Environment](./../postman/Environment.json) files.
2. **Import:** Open Postman -> Import -> Select both files.
3. **Environment:** Select `CinePlex_Local` from the environment dropdown.
4. **Token Handling:** Simply run the `Auth > Login` request. Our collection includes a script to automatically update the `{{access_token}}` variable for all subsequent requests.

---

## 📌 Base Infrastructure

- **Local Development**: `http://localhost:8888`
- **API Version**: `v1`
- **Swagger Documentation**: `/api/v1/swagger-ui.html`

## 📦 Response Formats

<details>
<summary><b>Standard Response</b></summary>

```json
  {
    "code": 200,
    "message": "Success",
    "data": { ... }
  }
```

</details>

<details>

<summary><b>Paginated Response</b></summary>

```json
  {
    "code": 200,
    "message": "Success",
    "data": {
      "content": [ ... ],
      "pagination": {
        "pageNumber": 0,
        "pageSize": 20,
        "totalElements": 10,
        "totalPages": 1
      }
    }
  }
```

</details>

### <span id="authentication"></span> 🔑 Authentication

| Method                                                              | Endpoint                     | Description          | Access                                                                  |
| :------------------------------------------------------------------ | :--------------------------- | :------------------- | :---------------------------------------------------------------------- |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/auth/register`      | Register new user    | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/auth/login`         | Get JWT token        | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/auth/logout`        | Clear JWT token      | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/auth/refresh-token` | Get JWT access token | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |

### <span id="movies"></span> 🎬 Movies

| Method                                                                  | Endpoint                    | Description           | Access                                                                  |
| :---------------------------------------------------------------------- | :-------------------------- | :-------------------- | :---------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/movie`             | Get all movies        | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/movie/{id}`        | Get movie by ID       | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/movie/filmId/{id}` | Get movie by movie ID | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square)     | `/api/v1/movie`             | Add movie             | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square)       | `/api/v1/movie/{id}`        | Update movie          | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![DELETE](https://img.shields.io/badge/DELETE-D32F2F?style=flat-square) | `/api/v1/movie/{id}`        | Delete movie          | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |

### <span id="theaters"></span> 🏛️ Theaters

| Method                                                                  | Endpoint                                     | Description                 | Access                                                                  |
| :---------------------------------------------------------------------- | :------------------------------------------- | :-------------------------- | :---------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/theater`                            | Get all theaters            | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/theater/{id}`                       | Get theater by ID           | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/theater/search?location={location}` | Search theaters by location | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square)     | `/api/v1/theater`                            | Add theater                 | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square)       | `/api/v1/theater/{id}`                       | Update theater              | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![DELETE](https://img.shields.io/badge/DELETE-D32F2F?style=flat-square) | `/api/v1/theater/{id}`                       | Delete theater              | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |

### <span id="showtimes"></span> 📅 Showtimes

| Method                                                                  | Endpoint                                      | Description                     | Access                                                                  |
| :---------------------------------------------------------------------- | :-------------------------------------------- | :------------------------------ | :---------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/showtime?date={date}`                | Get showtime by date            | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/showtime/movies/{movieId}`           | Get showtime by movie           | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/showtime/theaters/{theaterId}`       | Get showtime by theater         | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/showtime/available`                  | Get available showtime          | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/showtime/available/movies/{movieId}` | Get available showtime by movie | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square)     | `/api/v1/showtime`                            | Add showtime                    | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square)       | `/api/v1/showtime/{id}`                       | Update showtime                 | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![DELETE](https://img.shields.io/badge/DELETE-D32F2F?style=flat-square) | `/api/v1/showtime/{id}`                       | Delete showtime                 | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |

### <span id="seats"></span> 💺 Seats

| Method                                                            | Endpoint                                         | Description                    | Access                                                                                |
| :---------------------------------------------------------------- | :----------------------------------------------- | :----------------------------- | :------------------------------------------------------------------------------------ |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/seats/showtimes/{showtimeId}`           | Get all seats                  | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square)               |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/seats/showtimes/{showtimeId}/available` | Get available seats            | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/seats/reservation/{reservation}`        | Get all seat by reservation ID | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |

### <span id="articles"></span> 📝 Article

| Method                                                                  | Endpoint               | Description       | Access                                                                  |
| :---------------------------------------------------------------------- | :--------------------- | :---------------- | :---------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/article`      | Get all article   | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)       | `/api/v1/article/{id}` | Get article by ID | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square)     | `/api/v1/article`      | Add article       | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square)       | `/api/v1/article/{id}` | Update article    | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |
| ![DELETE](https://img.shields.io/badge/DELETE-D32F2F?style=flat-square) | `/api/v1/article/{id}` | Delete article    | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)   |

### <span id="payment"></span> 💳 Payment

| Method                                                              | Endpoint                 | Description                | Access                                                                  |
| :------------------------------------------------------------------ | :----------------------- | :------------------------- | :---------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)   | `/api/v1/payment/create` | Create payment 3rd API URL | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/payment/update` | Update payment             | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |

### <span id="reservation"></span> 🎟️ Reservations

| Method                                                              | Endpoint                                     | Description               | Access                                                                                                                                      |
| :------------------------------------------------------------------ | :------------------------------------------- | :------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------ |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)   | `/api/v1/reservation`                        | Get all reservation       | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)                                                                       |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)   | `/api/v1/reservation/{id}`                   | Get reservation by ID     | ![Owner](https://img.shields.io/badge/Owner-495057?style=flat-square) ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)   | `/api/v1/reservation/me`                     | Get own reservation by ID | ![Owner](https://img.shields.io/badge/Owner-495057?style=flat-square)                                                                       |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square)   | `/api/v1/reservation/{id}/restore`           | Get restore reservation   | ![Owner](https://img.shields.io/badge/Owner-495057?style=flat-square) ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/reservation/init`                   | Init a new reservation    | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square)                                                       |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/reservation/seatHold`               | Choose and booking seat   | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square)                                                       |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/reservation/confirm`                | Create reservation        | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square)                                                       |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/reservation/{reservationId}/cancel` | Cancel reservation        | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square)                                                       |

### <span id="dashboard"></span> 📊 Dashboard

| Method                                                            | Endpoint                                 | Description               | Access                                                                |
| :---------------------------------------------------------------- | :--------------------------------------- | :------------------------ | :-------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/dashboard/metrics`              | Get dashboard metrics     | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/dashboard/revenue`              | Get dashboard revenue     | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/dashboard/top-movies`           | Get top 5 movie           | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/dashboard/top-theaters`         | Get top 5 theater         | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/dashboard/payment-distribution` | Get payment distributions | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |

### <span id="report"></span> 📈 Report

| Method                                                            | Endpoint                         | Description   | Access                                                                |
| :---------------------------------------------------------------- | :------------------------------- | :------------ | :-------------------------------------------------------------------- |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/report/export/{format}` | Export report | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square) |

### <span id="upload"></span> 📤 Upload

| Method                                                              | Endpoint                    | Description          | Access                                                                  |
| :------------------------------------------------------------------ | :-------------------------- | :------------------- | :---------------------------------------------------------------------- |
| ![POST](https://img.shields.io/badge/POST-4ea1d3?style=flat-square) | `/api/v1/cloudinary/upload` | Upload to cloudinary | ![Public](https://img.shields.io/badge/Public-6c757d?style=flat-square) |

### <span id="users"></span> 🧑 Users

| Method                                                            | Endpoint                           | Description           | Access                                                                                |
| :---------------------------------------------------------------- | :--------------------------------- | :-------------------- | :------------------------------------------------------------------------------------ |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/user`                     | Get all user          | ![Admin](https://img.shields.io/badge/Admin-343a40?style=flat-square)                 |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/user/{id}`                | Get user by ID        | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/user/email/{email}`       | Get user by email     | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
| ![GET](https://img.shields.io/badge/GET-6DB33F?style=flat-square) | `/api/v1/role/{roleName}`          | Get user role         | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square) | `/api/v1/user/update/{id}/status`  | Update user by status | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
| ![PUT](https://img.shields.io/badge/PUT-FFB300?style=flat-square) | `/api/v1/user/update/{id}/profile` | Update user by id     | ![Authenticated](https://img.shields.io/badge/Authenticated-4a4e69?style=flat-square) |
