# Online Shopping Application Backend

This is a Spring Boot backend application for an online shopping platform. It provides RESTful endpoints to support user registration, login, order management (for both buyers and sellers), product management, watchlists, and JWT-based security.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Endpoints](#endpoints)
- [Frontend Integration](#frontend-integration)
- [License](#license)

## Features

- **User Authentication:**
  - Registration and login endpoints with JWT token generation.
  - Each user has a single role (e.g., ROLE_BUYER or ROLE_SELLER).

- **Order Management (Buyer):**
  - Place orders with multiple order items.
  - View order summaries and order details.
  - Cancel orders (with stock restoration).

- **Product Management:**
  - Retrieve products in stock.
  - Support for product details and listing.
  - Administrative endpoints for adding, updating, and managing products.

- **Watchlist:**
  - Users can add products to their watchlist.
  - Unique constraint prevents duplicates.

- **Seller Dashboard:**
  - Admin endpoints for managing orders (complete, cancel, view details).
  - Seller-specific services to view product details, summary of top profit product, popular products, and total items sold.

- **Security:**
  - Spring Security with JWT for securing endpoints.
  - Custom JWT filter and authentication entry point.

## Technologies

- **Backend Framework:** Spring Boot 2.7.0
- **ORM:** Hibernate / JPA
- **Database:** MySQL
- **Security:** Spring Security + JWT
- **Build Tool:** Maven
- **Other Libraries:** Lombok, JJWT

## Getting Started

### Prerequisites

- **Java:** JDK 11 or higher
- **MySQL:** Ensure MySQL is installed and running.
- **Maven:** Make sure Maven is installed.

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/JiachengZhao98/Online_Shopping_App.git
   cd Online_Shopping_App
   ```

2. **Configure the Database:**

   - Create a MySQL database (e.g., `shoppingdb`).
   - Update the `src/main/resources/application.properties` file with your database credentials.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/shoppingdb?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Build the Application:**

   ```bash
   mvn clean install
   ```

## Running the Application

Start the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will start on the default port `8080`. You should see log messages indicating that the application context has started and that Hibernate has updated the schema.

## Endpoints

### Authentication
- **POST** `/api/auth/register`
  Registers a new user.
  _Expected JSON payload:_
  ```json
  {
    "username": "buyer1",
    "email": "buyer1@example.com",
    "password": "your_password"
  }
  ```
- **POST** `/api/auth/login`
  Authenticates a user and returns a JWT token.
  _Expected JSON payload:_
  ```json
  {
    "username": "buyer1",
    "password": "your_password"
  }
  ```

### Buyer Endpoints
- **POST** `/api/orders`
  Place an order.
- **GET** `/api/orders/user`
  Retrieve all orders for the logged-in user.
- **GET** `/api/orders/{orderId}`
  Retrieve detailed information for a specific order.
- **PATCH** `/api/orders/cancel?orderId=123`
  Cancel a specific order.

### Product Endpoints
- **GET** `/api/products`
  Retrieve all products in stock.
- **GET** `/api/products/{productId}`
  Retrieve detailed product information.
- **POST** `/api/products/watchlist`
  Add a product to the watchlist.

### Seller Endpoints
- **GET** `/api/seller/orders`
  Retrieve orders for seller dashboard (with pagination).
- **PATCH** `/api/seller/orders/{orderId}/complete`
  Complete an order.
- **PATCH** `/api/seller/orders/{orderId}/cancel`
  Cancel an order.
- **GET** `/api/seller/product/{productId}`
  Retrieve product detail for seller.
- **POST** `/api/seller/product`
  Add a new product.
- **PATCH** `/api/seller/product/{productId}`
  Update a product.
- **GET** `/api/seller/summary`
  Retrieve summary data (top profit product, top popular products, total items sold).

## Frontend Integration

The Angular frontend communicates with this backend via REST endpoints. For development, configure an Angular proxy to forward `/api` requests to `http://localhost:8080`. In production, build your Angular app and serve it as static content from the Spring Boot application.

## License

This project is licensed under the MIT License - see the [LICENSE](https://en.wikipedia.org/wiki/MIT_License) file for details.
