
# ManZone Backend

## Project Overview

Welcome to the ManZone Backend repository! This project serves as the robust API (Application Programming Interface) for the ManZone e-commerce platform, specializing in accessories and cosmetic products for men. It handles all core functionalities, including product management, user authentication, order processing, and more, providing a reliable data source for the ManZone frontend application.

## Features

-   **User Authentication & Authorization:** Secure user registration, login, and role-based access control (e.g., customer, admin).
    
-   **Product Management:** CRUD (Create, Read, Update, Delete) operations for products, including categories, attributes, images, and pricing.
    
-   **Order Processing:** Management of shopping carts, order creation, status updates, and order history.
    
-   **Payment Integration (Placeholder):** Hooks for integrating with ZaloPay.
    
-   **Search & Filtering:** Robust search capabilities and advanced filtering options for products.
    
-   **Review & Rating System:** Users can submit reviews and ratings for products.
    
-   **Admin Dashboard APIs:** Endpoints for administrative tasks like managing users, products, and orders.
    

## Technologies Used

This backend is built with a modern and scalable technology stack:

-   **Language:** Java 24
    
-   **Framework:** Spring Boot 3.5.3
    
-   **Database:** MySQL
    
-   **ORM/ODM (if applicable):** Spring Data JPA
    
-   **Authentication:** Spring Security, JWT (Nimbus JOSE + JWT), OAuth2 Resource Server
    
-   **Other Libraries:** Lombok, Springdoc OpenAPI (Swagger UI), MapStruct, Spring Security Crypto
    

## Prerequisites

Before you begin, ensure you have the following installed on your system:

-   Java Development Kit (JDK) 24
    
-   Maven (usually bundled with IDEs like IntelliJ, Eclipse, or available via command line)
    
-   MySQL Server
    
## Getting Started

Follow these steps to get the ManZone Backend up and running on your local machine.

### 1. Clone the Repository

```
git clone https://github.com/huytqse170597/manzone.git
cd manzone

```

### 2. Install Dependencies

Install the necessary project dependencies:

```
# For Spring Boot projects, Maven will handle dependencies automatically
# when you build the project.

```

### 3. Configuration Files

Spring Boot applications typically use `application.properties` for configuration.

#### `src/main/resources/application.properties`

Create or update the `application.properties` file in `src/main/resources/` with the following content:

```
spring.application.name=manzone
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3308/manzonedb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

jwt.signerKey=jwt_secret
jwt.validDuration=60

app.default.avatar=https://i.pinimg.com/736x/cd/4b/d9/cd4bd9b0ea2807611ba3a67c331bff0b.jpg

springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

```
### 4. Database Setup

#### For MySQL:

1.  **Create Database:**
    
    ```
    CREATE DATABASE manzonedb;
    
    ```
    
    (Note: The `createDatabaseIfNotExist=true` in `spring.datasource.url` can handle this automatically if the user has sufficient privileges, but explicitly creating it is good practice.)

## Running the Application

Once everything is set up, you can start the backend server:

```
# Build the project (if not already built by your IDE)
mvn clean install

# Run the application
java -jar target/manzone-0.0.1-SNAPSHOT.jar
# or if using Maven directly
mvn spring-boot:run

```

The server should now be running, typically on `http://localhost:8080` (or the port specified in your `application.properties` file).

## Contributing

We welcome contributions to the ManZone Backend! Please follow these steps:

1.  Fork the repository.
    
2.  Create a new branch (`git checkout -b feature/your-feature-name`).
    
3.  Make your changes and ensure tests pass.
    
4.  Commit your changes (`git commit -m 'feat: Add new feature X'`).
    
5.  Push to the branch (`git push origin feature/your-feature-name`).
    
6.  Open a Pull Request.
    

Please ensure your code adheres to the project's coding standards and includes appropriate tests.
