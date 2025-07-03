# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/gradle-plugin/packaging-oci-image.html)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/reactive.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### User Management System using reactive programming
This project is a user management system built using Spring Boot and reactive programming principles. It provides a RESTful API for managing users, including operations for creating, reading, updating, and deleting user records.
It leverages Spring WebFlux for reactive programming and HashMap for data persistence. The project is designed to be scalable and efficient, making it suitable for high-load applications.
### Postman curl commands
#### Create a new user
```bash
curl -X POST http://localhost:8080/api/users \
     -H "Content-Type: application/json" \
     -d '{"name":"Alice","email":"alice@example.com"}'
```
#### Get all users
```bash
curl -X GET http://localhost:8080/api/users
```
#### Get a user by ID
```bash
curl -X GET http://localhost:8080/api/users/{id}
```
#### Update a user
```bash
curl -X PUT http://localhost:8080/api/users/{id} \
     -H "Content-Type: application/json" \
     -d '{"name":"Alice","email":"alice@example.com"}'
```
#### Delete a user
```bash
curl -X DELETE http://localhost:8080/api/users/{id}
```
### Running the Application
To run the application, you can use the following command:

```bash
./gradlew bootRun
```
### Building the Application
To build the application, you can use the following command:

```bash
./gradlew build
```
### Packaging the Application
To package the application as a JAR file, you can use the following command:

```bash
./gradlew bootJar
```
### Packaging the Application as an OCI Image
To package the application as an OCI image, you can use the following command:

```bash
./gradlew bootBuildImage
```
### Testing the Application
To run the tests for the application, you can use the following command:

```bash
./gradlew test
```
### Running Tests
To run the tests for the application, you can use the following command:

```bash
./gradlew test
```
### Running the Application in Development Mode
To run the application in development mode, you can use the following command:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```



