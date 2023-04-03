# spotify-coding-challenge
The goal of the challenge is to demonstrate knowledge and skills in interfacing with an external API.

## Basic

Fetch a few artists and their albums from the `Spotify API`. Those fetched data is stored in a database.
An API should then allow CRUD-Capability on those fetched artists and albums.  

### Acceptance Criteria
- Fetch data from API at the startup of the app
- Provide CRUD-Endpoints
- Use `PostgreSQL` inside a Docker Container
- Use `Flyway` to create the database structure
- Add endpoint to provide full-text search functionality
- Artists are fetched by a periodic background task
- Stored data that were modified by API may not be updated by the task
- Cover your code with tests 

## Solution approach

The application runs on Spring Boot 2.7. In the beginning the basic CRUD endpoints have been created.
The related database is a `PostgreSQL` running in a docker container which is started using a `docker-compose.yaml`.
The related database structures have been created and versioned using `Flyway`.


The manual testing was then done using the `Swagger UI` provided by the `Open API` documentation.
For the integration test [testcontainers](https://www.testcontainers.org/) were used to ensure data integrity for each run.


For requesting the Spotify API `Spring Cloud OpenFeign` is used, since it is a very lightweight solution
and fulfills all requirements, especially the handling of authentication.
Since the integration test should not rely on the productive `Spotify API`
a `Wiremock` `testcontainer` was introduced to handle all request.

The full-text search functionality for the name of the artist was implemented using `Hibernate-Search 6`
and an`Elasticsearch`
which was running in a docker container as well. For testing a new `testcontainer` with `Elasticsearch` has been introduced.


The acceptance criteria of fetching the data on startup and on a periodical basis are implemented using Spring features
namely `@PostConstruct` and `@Scheduled`

___
To use the application properly the following properties must be set
```
spring.security.oauth2.client.registration.spotify-developer.client-id
spring.security.oauth2.client.registration.spotify-developer.client-secret
```
The values should be retrieved following the Spotify documentation
[Getting-started](https://developer.spotify.com/documentation/web-api/tutorials/getting-started)

Furthermore the docker containers should be started using the `docker-compose.yaml`
## Out of scope / Open issues
- Entity and Dto validation especially ensuring no duplication of artists and albums
- Further search functionality f.e. the album name
- The image url should be a `@OneToMany` relationship since multiple images can be obtained by the `Spotify API`
- Add description to the endpoint such that the `Open API` documentation is more telling
- Extract the integration test into an own module or maven goal to run independently

## Further Reads/ Help Links
- https://developer.spotify.com/documentation/web-api
- https://www.baeldung.com/spring-rest-openapi-documentation
- https://www.baeldung.com/spring-cloud-openfeign
- https://www.baeldung.com/spring-cloud-feign-oauth-token
- https://rieckpil.de/spring-boot-integration-tests-with-wiremock-and-junit-5/
- https://www.baeldung.com/database-migrations-with-flyway
- https://www.spring-boot.io/blog/testcontainers-in-spring-boot-3-einfache-schritte-zur-erfolgreichen-einbindung
- https://www.baeldung.com/spring-scheduled-tasks
- https://www.testcontainers.org/modules/elasticsearch/
- https://docs.jboss.org/hibernate/search/6.0/migration/html_single/
- https://www.baeldung.com/hibernate-search
