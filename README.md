# PrisonGuardApp

## Overview
PrisonGuardApp is a comprehensive Java application designed to manage complex relationships between guards, prisoners, and visitors in a prison. 
Built on Spring Boot, the application utilizes a modular architecture with separate modules for Guards, Prisoners, and Visitors, ensuring scalability, ease of management, and separation of concerns. 
The architecture employs **Feign Client** for communication between modules and relational databases for data storage and management. 
Each module handles specific responsibilities, while the **Guards** module acts as the central coordinator for managing the entire application.

---

## Features
- **Guards Management**: Add, update, view, and delete guard details, including their activity status and permissions.
- **Prisoners Management**: Add, update, view, and delete prisoner details such as incarceration dates, imprisonment rigor, and addresses.
- **Visitors Management**: Add, update, view, and delete visitor details.
- **Guard-Controlled Relationships**: Assign prisoners to visitors and dynamically manage these relationships.
- **Cross-Module Communication**: Guards manage both prisoners and visitors, providing a unified interface to handle relationships.

---

## Architecture
### Modular Design
- **Guards**: Central module controlling the entire system.
- **Prisoners**: Module responsible for storing and managing prisoner details.
- **Visitors**: Module responsible for visitor records.

### Integration
- Communication between modules is facilitated using **Feign Client** for seamless integration.
- Data sharing is managed through **DTOs** (Data Transfer Objects) to ensure flexibility and decoupling.

### Database Management
- Each module uses a separate relational database table for its domain data.
- Guards manage relationships but do not directly modify the data of other modules.

---

## Tech Stack
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Communication**: Spring Cloud OpenFeign
- **Build Tool**: Maven
- **Logging**: SLF4J with Logback

---

## Endpoints Overview

### Guards Module
| Method | Endpoint          | Description                                |
|--------|-------------------|--------------------------------------------|
| GET    | `/guard/all`      | Retrieve all guards.                      |
| GET    | `/guard/{id}`     | Retrieve a specific guard by ID.          |
| POST   | `/guard/register` | Register a new guard.                     |
| POST   | `/guard/update/{id}` | Update details of an existing guard.  |
| POST   | `/guard/assign-prisoner` | Assign a prisoner to a visitor.   |

### Guards Controlling Prisoners
| Method | Endpoint                         | Description                                |
|--------|----------------------------------|--------------------------------------------|
| GET    | `/guard/prisoner/all`           | Retrieve all prisoners.                   |
| GET    | `/guard/prisoner/{id}`          | Retrieve details of a specific prisoner.  |
| POST   | `/guard/prisoner/register`      | Add a new prisoner.                       |
| POST   | `/guard/prisoner/update/{id}`   | Update prisoner details.                  |
| DELETE | `/guard/prisoner/delete/{id}`   | Delete a prisoner.                        |
| GET    | `/guard/prisoner/{id}/visitors` | Retrieve all visitors assigned to a specific prisoner. |

### Guards Controlling Visitors
| Method | Endpoint                         | Description                                |
|--------|----------------------------------|--------------------------------------------|
| GET    | `/guard/visitor/all`            | Retrieve all visitors.                    |
| GET    | `/guard/visitor/{id}`           | Retrieve details of a specific visitor.   |
| POST   | `/guard/visitor/register`       | Add a new visitor.                        |
| POST   | `/guard/visitor/update/{id}`    | Update visitor details.                   |
| DELETE | `/guard/visitor/delete/{id}`    | Delete a visitor.                         |

### Prisoners Module
| Method | Endpoint        | Description                                |
|--------|-----------------|--------------------------------------------|
| GET    | `/prisoner/all` | Retrieve all prisoners.                   |
| GET    | `/prisoner/{id}`| Retrieve details of a specific prisoner.  |
| POST   | `/prisoner/add` | Add a new prisoner.                       |
| POST   | `/prisoner/update/{id}` | Update prisoner details.           |
| DELETE | `/prisoner/delete/{id}` | Delete a prisoner.                 |

### Visitors Module
| Method | Endpoint         | Description                                |
|--------|------------------|--------------------------------------------|
| GET    | `/visitor/all`   | Retrieve all visitors.                    |
| GET    | `/visitor/{id}`  | Retrieve details of a specific visitor.   |
| POST   | `/visitor/add`   | Add a new visitor.                        |
| POST   | `/visitor/update/{id}` | Update visitor details.             |
| DELETE | `/visitor/delete/{id}` | Delete a visitor.                   |

---

## How Relationships Work
- **Dynamic Visitor-Prisoner Assignment**:
    - A guard can assign a prisoner to a visitor dynamically via the `/guard/assign-prisoner` endpoint.
    - Relationships are not predefined and are established after both a prisoner and a visitor are created.
- **Displaying Relationships**:
    - A guard can fetch all visitors assigned to a specific prisoner using the `/guard/prisoner/{id}/visitors` endpoint.

---

## Installation
1. Clone the repository:
   git clone https://github.com/piotrba/prisonGuardApp.git
2. Navigate to the project directory:
   cd prisonGuardApp 
3. Set up the databases for Guards, Prisoners, and Visitors modules in MySQL. 
4. Update application.properties files in each module with the appropriate database credentials. 
5. Build and run the modules:
   mvn clean install
   mvn spring-boot:run
6. Use Postman or another HTTP client to interact with the APIs.
