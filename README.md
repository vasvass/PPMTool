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
| Java | 21 |
| Spring Boot | 3.2.3 |
| Spring Security | (via Boot) |
| Spring Data JPA | (via Boot) |
| JJWT | 0.12.5 |
| MySQL | runtime |
| H2 (in-memory) | runtime |

### Frontend
| Technology | Notes |
|---|---|
| React | 18.3.1 |
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

| Tool | Version | Notes |
|---|---|---|
| Java | 21+ | `java -version` to verify |
| Maven | 3.9+ | Included via `./mvnw` wrapper — no separate install needed |
| Node.js | 18+ | `node -v` to verify |
| npm | 9+ | Bundled with Node.js |
| MySQL | 8+ | Only required for MySQL mode; skip if using H2 |

---

### Option A — Run with H2 (in-memory, no database setup)

This is the fastest way to get started. H2 runs embedded inside the Spring Boot process; data is lost when the server stops.

1. Comment out the MySQL lines and add an H2 datasource in `PPMTool/src/main/resources/application.properties`:

   ```properties
   # H2 in-memory database
   spring.datasource.url=jdbc:h2:mem:ppmtooldb
   spring.datasource.driver-class-name=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=

   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.jpa.hibernate.ddl-auto=create-drop
   spring.h2.console.enabled=true
   ```

2. Start the backend (from the repo root):

   ```bash
   cd PPMTool
   ./mvnw spring-boot:run
   ```

   The API is available at `http://localhost:8080`.
   The H2 browser console is available at `http://localhost:8080/h2-console`
   (JDBC URL: `jdbc:h2:mem:ppmtooldb`, username: `sa`, password: leave blank).

---

### Option B — Run with MySQL

#### 1. Create the database and user

Log in to MySQL as root and run:

```sql
CREATE DATABASE ppmtooldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ppmtoolusr'@'localhost' IDENTIFIED BY 'ppmtoolpwd';
GRANT ALL PRIVILEGES ON ppmtooldb.* TO 'ppmtoolusr'@'localhost';
FLUSH PRIVILEGES;
```

> Change the password before deploying anywhere non-local.

#### 2. Verify `application.properties`

The defaults in `PPMTool/src/main/resources/application.properties` already match the credentials above:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ppmtooldb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username=ppmtoolusr
spring.datasource.password=ppmtoolpwd

spring.jpa.hibernate.ddl-auto=update   # creates/updates tables automatically on startup
```

To override the JWT secret without editing the file, set an environment variable:

```bash
export APP_JWT_SECRET=your-long-random-secret-here
```

#### 3. Start the backend

```bash
cd PPMTool
./mvnw spring-boot:run
```

On Windows:

```bat
cd PPMTool
mvnw.cmd spring-boot:run
```

The API is available at `http://localhost:8080`.
On first run Hibernate will create all tables automatically (`ddl-auto=update`).

---

### Frontend Setup

#### 1. Install dependencies

```bash
cd ppmtool-react-client
npm install
```

#### 2. Start the dev server

```bash
npm start
```

The app opens at `http://localhost:3000`.
All `/api/*` requests are automatically proxied to `http://localhost:8080` via the `"proxy"` field in `package.json` — no CORS issues during development.

#### 3. Build for production

```bash
npm run build
```

Outputs a static bundle to `ppmtool-react-client/build/`. Serve it from any static host or copy it into the Spring Boot `static/` resources folder to serve everything from port 8080.

---

### Running Both Together (quick reference)

Open two terminals:

```bash
# Terminal 1 — backend
cd PPMTool && ./mvnw spring-boot:run

# Terminal 2 — frontend
cd ppmtool-react-client && npm start
```

Navigate to `http://localhost:3000`, register a new account, and log in.

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

## Current Implementation Status

### Backend (fully implemented)

**Authentication & Security**
- User registration with email as username, BCrypt password encoding, and password-match validation
- JWT generation on login with claims: `id`, `username`, `fullName`; 1-hour expiration
- `JwtAuthenticationFilter` validates tokens on every request and populates the `SecurityContext`
- Stateless Spring Security configuration; CSRF disabled; CORS enabled globally
- Custom `JwtAuthEntryPoint` returns JSON `401` responses for unauthenticated requests

**Domain Model**
- `User` — implements `UserDetails`; owns a list of `Project` entities
- `Project` — has a unique project identifier (4–5 chars), name, description, start/end dates, and a `projectLeader` field (email of owner)
- `Backlog` — auto-created with each project; holds a `PTSequence` counter for auto-incrementing task IDs
- `ProjectTask` — work item with summary, acceptance criteria, status (`TO_DO` / `IN_PROGRESS` / `DONE`), priority (`1`=High / `2`=Medium / `3`=Low), due date, and a sequence ID (e.g. `PROJ-1`)

**Business Logic**
- All project and task operations validate that the authenticated user is the project owner
- Task IDs are auto-generated as `{PROJECTID}-{sequence}` (e.g. `MYAPP-3`)
- Tasks default to priority `3` (Low) and status `TO_DO` if not supplied
- Global exception handler converts custom exceptions (`ProjectIdException`, `ProjectNotFoundException`, `UsernameAlreadyExistsException`) into structured JSON error responses

### Frontend (fully implemented)

**Authentication**
- Register and Login forms with client-side and server-side error display
- JWT stored in `localStorage`; decoded on app load via `jwtDecode` to rehydrate Redux auth state
- `SecuredRoute` HOC redirects unauthenticated users to `/login`
- `Header` shows different nav links depending on auth state; displays the logged-in user's full name

**Project Management**
- Dashboard lists all projects belonging to the current user
- Add Project form (name, unique identifier, description, start/end dates)
- Update Project form pre-populated with existing values
- Delete project with confirmation dialog

**Backlog / Task Board**
- Kanban board with three columns: **To Do** (grey) | **In Progress** (blue) | **Done** (green)
- Tasks rendered as cards showing: sequence ID, priority badge (color-coded), summary, truncated acceptance criteria, and Update / Delete actions
- Add Project Task form with all task fields
- Update Project Task form pre-populated with existing values
- Delete task with confirmation dialog

**Redux State**
- Four slices: `security` (auth), `project` (projects), `backlog` (tasks), `errors` (form errors)
- Async actions via Redux Thunk; Axios for all API calls with JWT injected from localStorage
