# How to Run Docker with Database

## Initialize Docker container
- Run Docker Desktop;
- Run [docker-compose.yml](docker-compose.yml) file with command `docker-compose up` to initialize **_postgres_** and **_pgAdmin_** containers,
there are password and user config for postgres:

|   User   | Password |
|:--------:|:--------:|
| cs_admin |   1234   |
- Postgres run on port: 5432;
- PgAdmin run on port: 5050;

### Application Connection
- I wrote all the connection type into [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
file, so you can just start an application.
- [Schema.sql](src%2Fmain%2Fresources%2Fscripts%2Fschema.sql) and [data.sql](src%2Fmain%2Fresources%2Fscripts%2Fdata.sql) will create new tables and paste their values.