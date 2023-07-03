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

### API
## wallet
`POST http://localhost:8082/account`	create account

    Data format:
    ```
    {
      "email":"aaa@bbb.ccc",
      "amount":"123.45"
    }
    ```

`POST http://localhost:8082/deposit/{accountId}`	deposit account

    Data format:
    ```
    {
      "summa":"123.45"
    }
    ```

`POST http://localhost:8082/withdraw/{accountId}`	withfraw account

    Data format:
    ```
    {
      "summa":"123.45"
    }
    ```

`GET http://localhost:8082/account/balance/{accountId}`	withfraw account

`GET http://localhost:8082/account/{accountId}`	display account information by account Id

`GET http://localhost:8082/account`	display account information by eMail

    Data format:
    ```
    {
      "email":"aaa@bbb.ccc"
    }

`GET http://localhost:8082/accounts}`	display all accounts

`PUT http://localhost:8082/account`	update existing account

    Data format:
    ```
    {
	  "id":"123",
      "email":"aaa@bbb.ccc",
      "amount":"123.45"
    }
    ```

## betting
`GET http://localhost:8081/bets`	display all bets

`GET http://localhost:8081/bets/{accountId}`	display all bets for account

`POST http://localhost:8081/bet/place`	place a bet

    Data format:
    ```
    {
	  "accountId":"123",
      "stake":"10.00", [
	    {
	      "id":"111",
		  "odds":"1.8"
		},
	    {
	      "id":"112",
		  "odds":"1.75"
		}
	  ]
    }
    ```

`POST http://localhost:8081/bet/placeAsync`	place a bet asynchronously


    Data format:
    ```
    {
	  "accountId":"123",
      "stake":"10.00", [
	    {
	      "id":"111",
		  "odds":"1.8"
		},
	    {
	      "id":"112",
		  "odds":"1.75"
		}
	  ]
    }
    ```

### Tests

I covered by tests service layer (except gRpc client side).
Also controller tests are implemented. For couple of them running mySql is required.

### TODO

* Add swagger for easy testing