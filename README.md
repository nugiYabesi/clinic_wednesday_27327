# Clinic Wednesday

This repository contains the Spring Boot backend and React frontend in separate folders.

## Backend

The Spring Boot application is in `backend/`. From that folder, start it with:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`. The frontend expects the backend at `http://localhost:8081`.

## Frontend

The React frontend is in `frontend/`. From that folder, install dependencies and start Vite:

```bash
npm install
npm run dev
```

The offices page supports searching by office name or number and deleting records.

### Available Offices

![Available Offices page](frontend/public/available_offices.png)
