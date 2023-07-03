# BetPawa test task

## General information
This test task consist of 2 microservices (wallt and betting), which communicate with each other via gRpc.
3-d project (proto-common) is used for generartion required artifacts for gRps for both wallet and betting services.

## Not implemented (due to lack of time, but, basically, lack of brains):

* adding Swagger (don't understand why - in prev. project all was Ok)
* adding gRps client side unit tests (saw articles abiut this issue, it's possible, but haven't enought time)
* building project in Docker - project should be built manually before. 

## Used technologies and tools

* Java 11
* Gradle
* mySql
* SpringBoot 2
* gRpc
* Lombok
* Docker
* Docker-compose
* Spring Data JPA
* Some other small spring embedded things

## Supplier microservice API

There are 2 end points in this microservice
* To send custom person to queue
`POST http://localhost:8811/api/supplier/sent-person-to-queue`

    Data format:
    ```
    {
      "first_name":"ABC",
      "last_name":"123",
      "age": 44
    }
    ```
* To enable/disable scheduler for sending predefined persons from test-data file.
    
    `GET http://localhost:8811/api/supplier/enable-scheduler?enabled=false`
    
    Where `enabled` can be `true` or `false`
    
    If scheduler is enabled, then it begins to send to queue predefined persons from test data.
    Person list can be defined in separate properties file. Name ot this file can be set in properties-file or like environment variable. 
    Scheduler period also can be defined in properties file.

## Consumer microservice API

There are 2 end points in this microservice

* To see all persons from redis

    `GET http://localhost:8812/api/consumer/get-redis-person-list`
    
* To see all persons from DB

    `GET http://localhost:8812/api/consumer/get-db-person-list`

## Sender configuration

* Configurations in `application.properties` file:
    ```
    test-data=classpath:test-data.properties
    scheduler-period=2000
    ```
    * `test-data` is link to test data. See below.
    * `scheduler-period` is period of scheduler sender, to send persons from predefined list.
* Configurations in `test-data.properties` file:
    ```
    test-data.calculation-seed=0.5
    
    test-data.person[2].firstName=Dmitry
    test-data.person[2].lastName=Ivanovich
    test-data.person[2].age=4
    ```
    * `test-data.calculation-seed` is calculation seed from the test task document.
    *  Configurations for predefined persons for scheduler.
        ```
          test-data.person[2].firstName=Dmitry
          test-data.person[2].lastName=Ivanovich
          test-data.person[2].age=4
        ```
        For now there is 7 persons


## How to run project

This project can be run in 2 ways:
* With docker-compose
* Like local applications

### Run with docker-compose
Re: I coudn't implement on-container building, so before running project need build project before :

`gradlew bootJar`

Then run the command :
`docker-compose up -d`

Docker-compose will start those docker images needed for this project :
* mySql
* wallet
* betting

### Run like local applications

Before run (consumer) and (supplier) applications, you need provide environment for them:

wallet needs :

* mySql (localhost:3306)

betting needs:

* mySql (localhost:3306)


### Tests

I covered by tests service layer (except gRpc client side).
Also controller tests are implemented. For couple of them running mySql is required.

### TODO

* Add swagger for easy testing