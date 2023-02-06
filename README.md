![Application Diagram](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/logoCard2Brain.png)
# Card2Brain Learn Smart
This project is the Backend Functionality of a Card Learning app.

With the app you are able to Sign Up a user with login.
Each User can manage his categories where he can add learning cards.
In the Learning mode, you can learn cards by categories. The Backend calculates which of the cards you should learn, according how many times the where answered correct or wrong.
Categories and cards are Changeable and deletable in the change mode.

## Features

* User Management
* Login
* Different Categories
* Learning Mode
* Card Editor

## Technologies

* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* JPA
* H2 (for dev)
* MySQL
* OpenAPI
* JUnit 5
* Lombok
* Maven

![H2 Database](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/H2.png) ![MySQL](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/mySql.png) ![SpringBoot](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/Springboot.png) ![Lombok](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/lombok.png) ![Maven](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/maven.png) ![JWT](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/JWT.png) ![open API](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/openApi.png) ![Docker](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/docker.png) ![GitHub](https://github.com/Alexstuder/Card2Brain/blob/master/doc/logos/gitHub.png)

## Requirements

To get started with this project, you will need to have the following installed on your local machine:

* JDK 17
* Maven 3+
* MySQL

To build and run the project, follow these steps:

* Clone the repository: `git clone https://github.com/Alexstuder/Card2Brain.git`
* Navigate to the project directory: cd Card2Brain
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:9001.

<br/>

## Application Diagramm

![Application Diagram](https://github.com/Alexstuder/Card2Brain/blob/master/doc/diagramms/application.jpg)

## Entity Diagramm

![Entity Diagramm](https://github.com/Alexstuder/Card2Brain/blob/master/doc/diagramms/entity.jpg)

## Class Diagramm

![Entity Diagramm](https://github.com/Alexstuder/Card2Brain/blob/master/doc/diagramms/classDiagramm.jpg)

## Create a Docker Container, Run and Publish to DockerHub

Create first a jar with the build instruction. Then create a container. Replace **dockerHubID** with your **dockerhub id
**.

<br/>



-> The application will be available at http://localhost:9001.



<br/>

```
$  mvn clean package
$  docker build -t dockerHubID/card2Brain
$  docker run -p 9001/9001 --rm -it  dockerHubID/card2Brain
```

<br/>

Ctrl c will stop and delete the container.

<br/>



```
$  docker login
$  docker login --username dockerHubID --password 
$  docker push dockerHubID/card2Brain
```
<br/>

login to deployment platform with a container infrastructure:

<br/>



```
$  docker pull dockerHubID/card2Brain
```

##  docker-compose

Start the files with:

<br/>

Start with log output in the console

```
$  docker-compose -f docker-compose.yml up
```

<br/>

Start in detached mode

```
$  docker-compose -f docker-compose.yml up -d
```

<br/>

Delete containers:

```
$  docker-compose -f docker-compose.yml rm
```

<br/>


## authors
Alexander Studer
Niklaus Hänggi
Roman Joller




License
---------

The project was created in the context of CAS OOP in ZHAW

02.02.2023

