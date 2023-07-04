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
      "stake":"10.00",
	  "items":[
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
	    "items":{
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
	
`POST http://localhost:8081/bet/settle` settle the bet


    Data format:
    ```
    {
      "betItemId":112,
      "state":"WIN"
    }
    ```


### Tests

I covered by tests service layer (except gRpc client side).
Also controller tests are implemented. For couple of them running mySql is required.

### TODO

* Add swagger for easy testing
* Build project inside docker container (without local installed Java, Gradle etc )
* Add gRpc client unit tests`
