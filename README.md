# Personal Project Management Tool (PPMTool)

A full-stack project management application built with **Spring Boot** (backend) and **React/Redux** (frontend). Users can register, log in, create projects, and manage project tasks organized in a backlog board.

## Features

- **User Authentication** — JWT-based registration and login; sessions persist across page refreshes
- **Project Management** — Create, view, update, and delete projects scoped to the logged-in user
- **Project Board** — Kanban-style backlog with tasks organized by status (To Do, In Progress, Done)
- **Project Tasks** — Add, update, and delete tasks with priority, due date, and acceptance criteria
- **Secured Routes** — Frontend routes are protected; unauthenticated users are redirected to login

## Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 1.8 |
| Spring Boot | 2.1.3 |
| Spring Security | (via Boot) |
| Spring Data JPA | (via Boot) |
| JJWT | 0.9.1 |
| MySQL | runtime |
| H2 (in-memory) | runtime |

### Frontend
| Technology | Notes |
|---|---|
| React | 16.8 |
| Redux + Redux Thunk | State management |
| React Router DOM | Client-side routing |
| Axios | HTTP client |
| Bootstrap 4 | Styling |

## Project Structure

```
PPMTool/
├── PPMTool/                        # Spring Boot backend
│   └── src/main/java/com/vasvass/ppmtool/
│       ├── domain/                 # JPA entities (Project, Backlog, ProjectTask, User)
│       ├── repositories/           # Spring Data repositories
│       ├── services/               # Business logic
│       ├── web/                    # REST controllers
│       ├── security/               # JWT filter, provider, security config
│       ├── payload/                # Request/response DTOs
│       ├── validator/              # Custom validators
│       └── exceptions/             # Exception handling
└── ppmtool-react-client/           # React frontend
    └── src/
        ├── actions/                # Redux action creators
        ├── reducers/               # Redux reducers
        ├── components/
        │   ├── Layout/             # Header, Landing page
        │   ├── Project/            # Add/Update project forms
        │   ├── ProjectBoard/       # Backlog board and task components
        │   └── UserManagement/     # Login and Register forms
        └── securityUtils/          # SecuredRoute (private route wrapper)
```

## Getting Started

### Prerequisites
- Java 8+
- Maven
- Node.js & npm
- MySQL (or use the embedded H2 database for development)

### Backend Setup

1. Configure the database in `PPMTool/src/main/resources/application.properties`:
   ```properties
   # For MySQL:
   spring.datasource.url=jdbc:mysql://localhost:3306/ppmtooldb
   spring.datasource.username=your_user
   spring.datasource.password=your_password

   # JWT secret (change in production):
   app.jwtSecret=PPMToolSecretKeyToGenJWTs
   app.jwtExpirationInMs=3600000
   ```

2. Run the backend:
   ```bash
   cd PPMTool
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

### Frontend Setup

1. Install dependencies:
   ```bash
   cd ppmtool-react-client
   npm install
   ```

2. Start the dev server:
   ```bash
   npm start
   ```
   The app will be available at `http://localhost:3000`. API calls are proxied to `http://localhost:8080`.

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Login and receive a JWT token |

### Projects
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/project/all` | Get all projects for the current user |
| POST | `/api/project` | Create a new project |
| GET | `/api/project/{projectId}` | Get a project by ID |
| PUT | `/api/project/{projectId}` | Update a project |
| DELETE | `/api/project/{projectId}` | Delete a project |

### Project Tasks (Backlog)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/backlog/{backlog_id}` | Get all tasks for a project |
| POST | `/api/backlog/{backlog_id}` | Add a task to a project backlog |
| GET | `/api/backlog/{backlog_id}/{pt_id}` | Get a specific task |
| PATCH | `/api/backlog/{backlog_id}/{pt_id}` | Update a task |
| DELETE | `/api/backlog/{backlog_id}/{pt_id}` | Delete a task |

All project and task endpoints require a valid JWT token in the `Authorization: Bearer <token>` header.
