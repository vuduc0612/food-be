# Food Delivery Backend Project Structure

This project is organized according to a module-based model with the following directory structure:

## Main Directory Structure

```
food-delivery-back-end/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/food_delivery_app/food_delivery_back_end/
│   │   │       ├── config/                 # Application configuration
│   │   │       ├── constant/               # Constants and enums
│   │   │       ├── modules/                # Functional modules
│   │   │       ├── response/               # Standardized response classes
│   │   │       ├── security/               # Security configuration
│   │   │       └── utils/                  # Utilities
│   │   └── resources/
│   │       ├── static/                     # Static resources
│   │       ├── templates/                  # Templates (mail, etc.)
│   │       └── application.yml             # Application configuration
│   └── test/                               # Test source code
├── pom.xml                                 # Maven configuration
└── docker-compose.yml                      # Docker configuration
```

## Main Modules

The project is organized into the following functional modules:

### 1. Auth Module

```
modules/auth/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── entity/                 # Entity classes
│   ├── Account.java        # Account information
│   └── AccountRole.java    # Account roles
├── repository/             # JPA Repositories 
└── service/                # Business Logic
```

### 2. User Module

```
modules/user/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects  
├── entity/                 # Entity classes
│   └── User.java           # User information
├── repository/             # JPA Repositories
└── service/                # Business Logic
```

### 3. Restaurant Module

```
modules/restaurant/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── entity/                 # Entity classes
│   └── Restaurant.java     # Restaurant information
├── repository/             # JPA Repositories
└── service/                # Business Logic
```

### 4. Category Module

```
modules/category/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── entity/                 # Entity classes
│   └── Category.java       # Dish categories
├── repository/             # JPA Repositories
└── service/                # Business Logic
```

### 5. Dish Module

```
modules/dish/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── entity/                 # Entity classes
│   └── Dish.java           # Dish information
├── repository/             # JPA Repositories
└── service/                # Business Logic
```

### 6. Order Module

```
modules/order/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── entity/                 # Entity classes
│   ├── Order.java          # Order information
│   └── OrderDetail.java    # Order details
├── repository/             # JPA Repositories
└── service/                # Business Logic
```

### 7. Cart Module

```
modules/cart/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── service/                # Business Logic
└── redis/                  # Redis Cache for shopping cart
```

### 8. Statistics Module

```
modules/statistics/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
├── repository/             # Custom Repositories
└── service/                # Business Logic
```

### 9. Location Module

```
modules/location/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
└── service/                # Business Logic
```

### 10. OTP Module (One-Time Password)

```
modules/otp/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
└── service/                # Business Logic
```

### 11. Payment Module

```
modules/payment/
├── controller/             # REST Controller
├── dto/                    # Data Transfer Objects
└── service/                # Business Logic
```

## Main Entity Relationships

### Account and Authorization Relationships
- `Account` ↔ `AccountRole`: **One-to-Many**
- `Account` ↔ `User`: **One-to-One**
- `Account` ↔ `Restaurant`: **One-to-One**

### Restaurant and Dish Relationships
- `Restaurant` ↔ `Dish`: **One-to-Many**
- `Restaurant` ↔ `Category`: **One-to-Many**
- `Category` ↔ `Dish`: **One-to-Many**

### Order Relationships
- `User` ↔ `Order`: **One-to-Many**
- `Restaurant` ↔ `Order`: **One-to-Many**
- `Order` ↔ `OrderDetail`: **One-to-Many**
- `OrderDetail` ↔ `Dish`: **Many-to-One**

## Technologies Used

- **Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: MySQL
- **Cache**: Redis
- **Security**: Spring Security + JWT
- **Documentation**: Swagger/OpenAPI
- **Testing**: JUnit, Mockito
- **Containerization**: Docker 