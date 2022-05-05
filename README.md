# Inventory Basic System - BACKEND

Inventory is a web app for maintaining a product's inventory. It was developed by Luis Espinosa Llanos and for the backend it was used 
the following technologies and tools: 

<table style="width:100%">
  <tr>
    <td>
  	Core	
    </td>
    <td>
  	Java 11, Spring Boot 2, Data JPA, Hibernate, Loombok, Jackson Databinding, Spring Security, Java JWT.
    </td>
  </tr>
  <tr>
    <td>
  	Database	
    </td>
    <td>
  	PostgreSQL 11
    </td>
  </tr>
  <tr>
    <td>
  	Testing	
    </td>
    <td>
  	Junit 5, Mockito, Sonar Lint, SonarQube
    </td>
  </tr>
  <tr>
    <td>
  	Server	
    </td>
    <td>
  	Apache Tomcat Embebido (Spring Boot)
    </td>
  </tr>
  <tr>
    <td>
  	IDE	
    </td>
    <td>
  	Spring Tool Suite
    </td>
  </tr>
  <tr>
    <td>
  	Web Server	
    </td>
    <td>
  	Nginx
    </td>
  </tr>
  <tr>
    <td>
  	Performance Tests
    </td>
    <td>
  	JMeter
    </td>
  </tr>
  <tr>
    <td>
  	Executable	
    </td>
    <td>
  	Jar
    </td>
  </tr>
</table>

It was written using the best practices for instance, a controller, service and repository layer approach, code reusing, unit tests, 
a good condition coverage, dependecy injection, inversion of control, abstractions, token and Role based security, design patterns 
and more... 

## Where is the FrontEnd?
This project still has no FrontEnd.... But It will soon, of course developed in reactJS.

## Video
A video exposing the functionality of the proyect in local environment on a Desktop screen.

https://youtu.be/gGtKy-g_Dpc

## Development Resources
I provide the following resources:

<table style="width:100%">
  <tr>
    <td>
  	Database SQL Backup	
    </td>
    <td>
	In the db folder
    </td>
  </tr>
  <tr>
    <td>
  	Postman Collection	
    </td>
    <td>
	In the postman folder
    </td>
  </tr>
  <tr>
    <td>
  	Performance
    </td>
    <td>
	In the performance folder
    </td>
  </tr>
</table>


## Pictures
Some pictures of the project on a local environment respectively:

<table style="width:100%">
  <tr>
    <td>
  		<img width="450" alt="Eclipse" src="https://user-images.githubusercontent.com/56041525/166857208-2272fe51-180a-4791-af8e-296ba3bb8e36.PNG">
	  </td>
    <td>
  	<img width="450" alt="Response Time Graph" src="https://user-images.githubusercontent.com/56041525/166857311-6e905a1a-17c1-4baf-a541-7c4b03b09d4d.png">
    </td>
  </tr>
</table>


<table style="width:100%">
  <tr>
    <td>
  		<img width="450" alt="JMeter" src="https://user-images.githubusercontent.com/56041525/166857376-28ca8471-31c6-48d8-b846-2a6ad27dd3d1.PNG">
	  </td>
    <td>
	<img width="450" alt="SonarQube" src="https://user-images.githubusercontent.com/56041525/166857412-b6d8ae6b-00ff-4984-9e4a-050baaaa257a.PNG">
    </td>
  </tr>
</table>


<table style="width:100%">
  <tr>
    <td>
  		<img width="450" alt="Entity relationship model" src="https://user-images.githubusercontent.com/56041525/166857444-cf942cc6-a5ba-430e-8a61-d30abc2943ee.png">
	  </td>
    <td>
	<img width="450" alt="Heroku deployment" src="https://user-images.githubusercontent.com/56041525/166857493-c5b4730a-f9b5-49d9-9bf3-e642190897e9.PNG">
    </td>
  </tr>
</table>


<table style="width:100%">
  <tr>
    <td>
  		<img width="450" alt="PostgreSQL Table" src="https://user-images.githubusercontent.com/56041525/166858893-6e33fdbe-88a6-4f1b-921c-b2ff0aefc951.PNG">
	  </td>
    <td>
	<img width="450" alt="Categories" src="https://user-images.githubusercontent.com/56041525/166858999-62fae027-e31f-46f7-af16-cef122849288.PNG">
    </td>
  </tr>
</table>


# Want to give it a try? Here you have it Online
Please don't overuse it, because it is a FREE tier, I wouldn't want to incur in unnecesary expenses :), REMEBER that I've 
provided the complete POSTMAN collection:
NOTE: If the API is asleep, please contact me and I'll be happy to turn it on...
https://inventory-springboot-backend.herokuapp.com


## Installation

This proyect should be installed using the following command:
```bash
mvn clean install
```

## Usage
In the target folder you will find the Jar archive, of course the configuration for Login purposes was externalized, this is to say that
arguments have to be passed through command line, so please use the following command according to your values:

```bash
java -jar -DSECRET_KEY=securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure -DEXPIRATION_DAYS=800 -DSPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/inventory -DSPRING_DATASOURCE_USERNAME=inventoryadmin -DSPRING_DATASOURCE_PASSWORD=123456 -DSPRING_DATABASE_SCHEMA=app inventory-0.0.1-SNAPSHOT.jar
```

## Tests and coverage
In the project you will find the tests report with Jacoco. But you should scan it with SonarQube. Please be sure that your local 
SonarScanner is up.
```bash
mvn clean install sonar:sonar
```
You've got to see the following results on the dashboard, but of course as in the second image you could only use the coverage eclipse's 
plugin to get a glimpse of the overall coverage (Take into account that some clases were exluded from Sonar for instance DTOs):

<table style="width:100%">
  <tr>
    <td>
  		<img width="450" alt="SonarQube Project" src="https://user-images.githubusercontent.com/56041525/166857849-4cf04f01-4bd8-4190-acb3-edb0e3db67eb.PNG">
	  </td>
    <td>
	<img width="450" alt="Eclipse Coverage" src="https://user-images.githubusercontent.com/56041525/166858008-230187e3-640d-40fa-9d08-8e65b389dcdc.PNG">
    </td>
  </tr>
</table>


## Contributing
This proyect is quite simple, and is part of my personal portfolio, so it is not intended to receive contributions.


## License
It is free.
