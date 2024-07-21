# Tanda B2C

## Overview
A payments integration solution
## Setup
To set up the CRESWAVE_CODE_TEST application, ensure you have the following prerequisites:
1. Java Development Kit (JDK) installed on your machine.
2. MongoDB Compass installed and running locally.
3. Text editor or IDE for viewing and editing the application code.

## Configuration
The application uses configuration properties as specified in app.properties file. Below are key considerations:


```properties
spring.data.mongodb.uri=mongodb://localhost:27017/payments\
spring.data.mongodb.database=payments
```

## Kafka

1. Ensure Kafka is running on port 9092 or configure the app.properties to use whichever port kafka is running on.

## NGROK
1. Setup your ngrok to expose your localhost.
2. After this change the ngrok link at app.properties to your ngrok link. 

## Usage
1. Clone or download the application from the repository.
2. Open the application in your preferred IDE or text editor (I suggest InteliJ).
3. Configure the database connection properties in the `application.properties` file.
4. Run the application using Maven.
5. Access the RESTful APIs provided by the application to create, edit, and delete blogs and comments. It's recommended to use Postman for ease of use.

## Swagger
Users can also use Swagger for testing the endpoints if preferred.
Access the docs on: http://localhost:8080/swagger-ui/index.html#

