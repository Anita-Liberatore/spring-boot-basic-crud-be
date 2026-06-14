# crud-api-student-hateoas-be

A Spring Boot CRUD API for students with HATEOAS links, validation, centralized error handling, and logical delete through student status.

## Endpoints

CREATE A STUDENT --> POST http://localhost:8080/api/v1/students
READ ALL STUDENTS --> GET http://localhost:8080/api/v1/students
FILTER STUDENTS BY STATUS --> GET http://localhost:8080/api/v1/students?status=ACTIVE
READ A STUDENT BY ID --> GET http://localhost:8080/api/v1/students/{ID}
UPDATE A STUDENT --> PUT http://localhost:8080/api/v1/students/{ID}
DEACTIVATE A STUDENT --> DELETE http://localhost:8080/api/v1/students/{ID}
