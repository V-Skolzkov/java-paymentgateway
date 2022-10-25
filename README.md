# Payment Gateway

Take home assignment for a Java/Spring developer.

The detailed requirements are found in [ASSIGNMENT.md](ASSIGNMENT.md).

1. [Description](#description)
2. [How to run](#how-to-run)
3. [Service API](#service-api)

## Description

Payment Gateway is responsible for:
- receive, validate and persist payment into payment storage
- return payment by request

## How to run
Before run service need to do next:  
Set path for audit file in properties file src\main\resources\application.properties  
OR  
Set path to audit file in environment variables   
For example for Windows using next command:  
set file.path=C:/work/demo/audit.log

Service can be run using next command:

java -jar payment-gateway-0.1.0.jar --server.port=8080

## Service API

http://localhost:8080/payment-gateway/swagger-ui/index.html
