# Authentication service

## Requirements

For building and running the application you need:
*** If setting the env BUILD_ENV=public, gradle will download dependencies from public repository ***

- [JDK 11](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 7.2](https://maven.apache.org)

## How to run it
` ./gradlew build clean ` <br />
` ./gradlew bootRun ` <br />
` Change config usemock on production ` <br />


## Convention
* CAMEL CASE

## Base Response
  ```
     {
        status: 200,
        message: "Success",
        data: {}
     }
  ``` 
## Response status

| HTTP code     | Business code |     Message     |
| ------------- | ------------- |   ------------- |
| 200           |         1000  |   Success  |
| 200           |         1001  |   User does not have easy invest account  |
| 203           |         2003  |   Non-Authoritative Information  |
| 400           |         4000  |   Bad request  |
| 401           |         4001  |   Unauthorized  |
| 403           |         4001  |   Forbidden  |
| 404           |         4004  |   Not found  |
| 404           |         4004  |   Resource not found  |
| 409           |         4009  |  Conflict  |
| 500           |         5000  |   Internal server error  |

## Endpoint health service
* http://localhost:8897/api-swaggers.html for swagger api
* http://localhost:8897/api-docs for see api docs
* http://localhost:8897/manage for health service metric
