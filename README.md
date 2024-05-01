# Documentation for Clear Solutions Task

## Prerequisites
- Java 17 or higher
- Docker Desktop

## Initializing the Database
- How to initialize the database I describe in those file: [docker-db-initialization](Postgres_Docker_Init.md)

## Running the Web Application
1. Start Docker container;
2. Start Application: [ClearSolutionsApplication🟢](src%2Fmain%2Fjava%2Ftech%2Ftask%2Fclearsolutions%2FClearSolutionsApplication.java);
3. The application will be available at: `http://localhost:8080`.

## Endpoints
### Authentication (/api/v1/auth)
- **POST /signup**: Register a user. Accepts [UserCreateRequest](src%2Fmain%2Fjava%2Ftech%2Ftask%2Fclearsolutions%2Fdto%2FUserCreateRequest.java), checks users birthdate for valid. Returns User Response.
- **POST /login**: Authenticate a user. Accepts email and password. Returns user with access and refresh token.

### Users (/api/v1/user)
- **PATCH /special-fields**: Update special fields. Accepts [UserSpecialFieldsUpdate](src%2Fmain%2Fjava%2Ftech%2Ftask%2Fclearsolutions%2Fdto%2FUserSpecialFieldsUpdate.java), checks users birthdate for valid. Available only for same users!
- **PUT**: Update all users fields. Accepts [UserUpdateRequest](src%2Fmain%2Fjava%2Ftech%2Ftask%2Fclearsolutions%2Fdto%2FUserUpdateRequest.java), checks users birthdate for valid. Available only for same users!
- **DELETE /{id}**: Delete user. Accepts **id**. Available only for same users!
- **GET**: Get a list of users by dates. Accepts dates '_from_' and '_to_'.

### Logout
- **POST /api/v1/logout**: Logout user from system, if he authenticated. Return String.

## Swagger Documentation
- To check swagger documentation you can follow this link, while Application is running: http://localhost:8080/swagger-ui/index.html